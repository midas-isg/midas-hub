package edu.pitt.isg.midas.hub.report;

import edu.pitt.isg.midas.hub.hibernate.JsonbType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashMap;

@Entity
@Table(name = "auth0_log")
@TypeDef(name = JsonbType.NAME, typeClass = JsonbType.class, parameters = {
        @Parameter(name = JsonbType.CLASS, value = "java.util.HashMap")})
public class ReportingLog {
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    private String applicationId;
    private String applicationName;
    private String userId;
    private String userAffiliation;
    private String userName;
    private String userConnection;
    private String eventCode;
    private String event;

    private HashMap raw;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAffiliation() {
        return userAffiliation;
    }

    public void setUserAffiliation(String userAffiliation) {
        this.userAffiliation = userAffiliation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserConnection() {
        return userConnection;
    }

    public void setUserConnection(String userConnection) {
        this.userConnection = userConnection;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Type(type = JsonbType.NAME)
    public HashMap getRaw() {
        return raw;
    }

    public void setRaw(HashMap raw) {
        this.raw = raw;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ReportingLog{" +
                "raw=" + raw +
                '}';
    }
}