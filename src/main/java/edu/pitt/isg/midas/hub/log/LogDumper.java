package edu.pitt.isg.midas.hub.log;

import edu.pitt.isg.midas.hub.auth0.Auth0Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
class LogDumper {
    private static final Logger log = LoggerFactory.getLogger(LogDumper.class);

    @Autowired
    private LogRepository repository;
    @Autowired
    private Auth0Dao dao;
    @Autowired
    private LogTransformer transformer;

    void dump() {
        transformer.init(dao.listUsers(), dao.listApplications());
        // reformat();
        etl();
    }

    private void reformat() {
        final List<ReportingLog> all = repository.findAll();
        all.forEach(log -> save(log.getRaw()));
    }

    private void etl() {
        final int perPage = 50;
        int page = 0;
        List<HashMap<String, ?>> logLot;
        do {
            logLot = dao.listLogs(perPage, page);
            logLot.forEach(this::save);
            log("Read " + logLot.size() + " logs from page #: " + page);
            page++;
        } while (logLot.size() >= perPage);
        final int logs = perPage * (page - 1) + logLot.size();
        log.info(logs + " logs were dumped from auth0.com.");
    }

    private void save(HashMap<String, ?> record){
        ReportingLog log = transformer.toReportingLog(record);
        repository.save(log);
    }

    private void log(Object message) {
        log.debug(Objects.toString(message));
    }
}