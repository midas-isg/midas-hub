package edu.pitt.isg.midas.hub.affiliation;

public class AffiliationForm extends Affiliation {
    private String additionalAffiliationNames;
    private String[] affiliationGroups;

    public String getAdditionalAffiliationNames() {
        return additionalAffiliationNames;
    }

    public void setAdditionalAffiliationNames(String additionalAffiliationNames) {
        this.additionalAffiliationNames = additionalAffiliationNames;
    }

    public String[] getAffiliationGroups() {
        return affiliationGroups;
    }

    public void setAffiliationGroups(String affiliationGroups[]) {
        this.affiliationGroups = affiliationGroups;
    }
}
