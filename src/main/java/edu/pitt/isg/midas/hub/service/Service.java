package edu.pitt.isg.midas.hub.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Service {
    private static final int MAX_VARCHAR = 10485760; // due to PostgreSQL

    private Long id;
    private String name;
    private String description;
    private String url;
    private String softwareType;
    private String developmentGroup;
    private String contact;
    private String ssoEnabled;

    Service() {
    }

    public Service(String name, String url, String description, String softwareType, String developmentGroup, String contact) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.softwareType = softwareType;
        this.developmentGroup = developmentGroup;
        this.contact = contact;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = MAX_VARCHAR)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSoftwareType() {
        return softwareType;
    }

    public void setSoftwareType(String softwareType) {
        this.softwareType = softwareType;
    }

    public String getDevelopmentGroup() {
        return developmentGroup;
    }

    public void setDevelopmentGroup(String developmentGroup) {
        this.developmentGroup = developmentGroup;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSsoEnabled() {
        return ssoEnabled;
    }

    public void setSsoEnabled(String ssoEnabled) {
        this.ssoEnabled = ssoEnabled;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", softwareType='" + softwareType + '\'' +
                ", developmentGroup='" + developmentGroup + '\'' +
                ", contact='" + contact + '\'' +
                ", ssoEnabled='" + ssoEnabled + '\'' +
                '}';
    }
}