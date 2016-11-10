package edu.pitt.isg.midas.hub.auth0;

import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

class UrlAid {
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
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(auth0Domain)
                .pathSegment("api", "v2");
    }
}