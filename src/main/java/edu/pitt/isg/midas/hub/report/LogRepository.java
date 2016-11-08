package edu.pitt.isg.midas.hub.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<ReportingLog, Long> {
}