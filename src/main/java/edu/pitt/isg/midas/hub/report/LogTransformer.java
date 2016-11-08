package edu.pitt.isg.midas.hub.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.pitt.isg.midas.hub.auth0.EventAid.makeEventCode2NameLookupMap;

@Service
class LogTransformer {
    @Autowired
    private Factory factory;
    private Map<String, String> eventCode2Name;
    private Map<String, String> userId2AffiliationName;
    private Map<String, String> userId2Name;
    private Map<String, String> applicationId2Name;

    void init(List<HashMap<String, ?>> users, List<HashMap<String, ?>> applications) {
        eventCode2Name = makeEventCode2NameLookupMap();
        userId2AffiliationName = factory.makeUserId2AffiliationName(users);
        userId2Name = factory.makeUserId2Name(users);
        applicationId2Name = factory.makeApplicationId2Name(applications);
    }

    ReportingLog toReportingLog(HashMap<String, ?> record) {
        final ReportingLog log = new ReportingLog();
        log.setRaw(record);
        log.setId(str(record, "_id"));
        final String userId = str(record, "user_id");
        log.setUserId(userId);
        log.setUserAffiliation(userId2AffiliationName.get(userId));
        log.setUserName(userId2Name.get(userId));
        log.setUserConnection(toConnection(userId));
        final String clientId = str(record, "client_id");
        log.setApplicationId(clientId);
        log.setApplicationName(applicationId2Name.get(clientId));
        final String eventCode = str(record, "type");
        log.setEventCode(eventCode);
        log.setEvent(eventCode2Name.get(eventCode));
        log.setTimestamp(Date.from(toInstant(record, "date")));
        return log;
    }

    private String toConnection(String userId) {
        if (userId == null)
            return null;
        return userId.split("\\|")[0];
    }

    private Instant toInstant(HashMap<String, ?> record, String key) {
        final Object obj = record.get(key);
        if (obj == null)
            return null;
        return Instant.parse(obj.toString());
    }

    private String str(HashMap<String, ?> record, String key) {
        final Object obj = record.get(key);
        if (obj == null)
            return null;
        return obj.toString();
    }
}