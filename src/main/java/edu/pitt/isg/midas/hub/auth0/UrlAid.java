package edu.pitt.isg.midas.hub.auth0;

import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

public class UrlAid {
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

    public static String toAuth0UserUrl(String auth0Domain, String userId) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(auth0Domain)
                .pathSegment("api", "v2", "users", userId)
                .build().toString();
    }
}
