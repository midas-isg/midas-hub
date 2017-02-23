package edu.pitt.isg.midas.hub.auth0;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.RETURN_TO_URL_KEY;

class UrlAid {
    private static final String TITLE_KEY = "title";
    private static final String MESSAGE_KEY = "message";

    private UrlAid() {
    }

    private static UriComponentsBuilder toContextUrlBuilder(HttpServletRequest request) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(request.getScheme())
                .host(request.getServerName());
        final int serverPort = request.getServerPort();
        if (!(serverPort == 80 || serverPort == 443))
            uriBuilder.port(serverPort);

        final String contextPath = request.getContextPath();
        if (!(contextPath == null || contextPath.isEmpty()))
            uriBuilder.path(contextPath);

        return uriBuilder;
    }

    static String toAbsoluteUrl(HttpServletRequest request, String... pathSegments) {
        return toContextUrlBuilder(request).pathSegment(pathSegments).build().normalize().toString();
    }

    static String toRelativeUrl(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
    }

    static String toAuth0LogUrl(String auth0Domain, int perPage, int page) {
        return toAuth0ApiUrlBuilder(auth0Domain)
                .pathSegment("logs")
                .queryParam("sort", "date:1")
                .queryParam("page", page)
                .queryParam("perPage", perPage)
                .build().toString();
    }

    static String toAuth0UserUrl(String auth0Domain, String userId) {
        return toAuth0ApiUrlBuilder(auth0Domain)
                .pathSegment("users", userId)
                .build().toString();
    }

    static String toAllApplicationsUrl(String auth0Domain) {
        return toAuth0ApiUrlBuilder(auth0Domain)
                .pathSegment("clients")
                .build().toString();
    }

    static String toAllAuth0UsersUrl(String auth0Domain) {
        return toAuth0ApiUrlBuilder(auth0Domain)
                .pathSegment("users")
                .build().toString();
    }

    private static UriComponentsBuilder toAuth0ApiUrlBuilder(String auth0Domain) {
        return toAuth0UrlBuilder(auth0Domain)
                .pathSegment("api", "v2");
    }

    static UriComponentsBuilder toAuth0UrlBuilder(String auth0Domain) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(auth0Domain);
    }

    static String toEncodedReturnUrl(HttpServletRequest request, String appName) {
        final String returnUrl = toFinalReturnUrl(request);
        return encode(toSsoUrl(request, returnUrl, appName));
    }

    private static String toFinalReturnUrl(HttpServletRequest request) {
        final String url = request.getParameter(RETURN_TO_URL_KEY);
        return url != null ? url: UrlAid.toAbsoluteUrl(request, "login");
    }

    private static String encode(String url) {
        final String utf8 = "utf8";
        try {
            return UriUtils.encode(url, utf8).toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Cannot encode with " + utf8, e);
        }
    }

    private static String toSsoUrl(HttpServletRequest request, String returnUrl, String appName) {
        final String sso = UrlAid.toAbsoluteUrl(request, "sso");
        return UriComponentsBuilder.fromHttpUrl(sso)
                .queryParam(TITLE_KEY, encode(toTitle(request, appName)))
                .queryParam(MESSAGE_KEY, encode(toMessage(request)))
                .queryParam(RETURN_TO_URL_KEY, returnUrl)
                .build()
                .toString();
    }

    private static String toMessage(HttpServletRequest request) {
        final String message = request.getParameter(MESSAGE_KEY);
        if (message == null)
            return "Logged out successfully.";
        return message;
    }

    private static String toTitle(HttpServletRequest request, String appName) {
        final String title = request.getParameter(TITLE_KEY);
        if (title == null)
            return appName;
        return title;
    }
}