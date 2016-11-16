package edu.pitt.isg.midas.hub.auth0;

public class PredefinedStrings {
    public static final String APP_METADATA = "app_metadata";
    public static final String ROLES = "roles";

    public static final String AFFILIATION = "affiliation";

    // Roles
    public static final String ISG_USER = "ISG_USER";
    public static final String ISG_ADMIN = "ISG_ADMIN";

    // Spring access-control expression
    private static final String hasAuthority = "hasAuthority";
    public static final String IS_ISG_USER = hasAuthority + "('"+ ISG_USER + "')";
    public static final String IS_ISG_ADMIN = hasAuthority + "('"+ ISG_ADMIN + "')";

    public static final String RETURN_TO_URL_KEY = "returnToUrl";
}