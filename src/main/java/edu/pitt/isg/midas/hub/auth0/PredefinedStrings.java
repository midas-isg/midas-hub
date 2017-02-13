package edu.pitt.isg.midas.hub.auth0;

public class PredefinedStrings {
    public static final String APP_METADATA = "app_metadata";
    public static final String ROLES = "roles";

    public static final String AFFILIATION = "affiliation";

    // Roles
    public static final String ISG_USER = "ISG_USER";
    public static final String ISG_ADMIN = "ISG_ADMIN";

    // Spring access-control expression
    private static final String hasAnyAuthority = "hasAnyAuthority";
    public static final String IS_ISG_USER = hasAnyAuthority + "('"+ ISG_USER + "')";
    public static final String IS_ISG_ADMIN = hasAnyAuthority + "('"+ ISG_ADMIN + "')";


    // Roles for Accounts
    private static final String ACCOUNTS_DIRECTOR = "ACCOUNTS_DIRECTOR";
    // Generic Roles
    public static final String ACCOUNTS_APP_ADMIN = "ACCOUNTS_APP_ADMIN";
    public static final String CAN_VIEW_LOG_REPORTS = "hasAnyAuthority('" +
            ACCOUNTS_APP_ADMIN + "','" +
            ACCOUNTS_DIRECTOR  + "','" +
            ISG_ADMIN + "')";

    public static final String RETURN_TO_URL_KEY = "returnToUrl";
}