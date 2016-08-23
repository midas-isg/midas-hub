/*
 * Copyright (C) 2016 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package edu.pitt.isg.midas.hub.auth0;

import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Feb 18, 2016 12:18:13 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class UrlAid {

    private UrlAid() {
    }

    private static UriComponentsBuilder buildURI2(HttpServletRequest request) {
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String scheme = (serverPort == 443) ? "https" : "http";  // http or https
        String contextPath = request.getContextPath();  // /ccd

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(serverName);
        if (!(serverPort == 80 || serverPort == 443)) {
            uriBuilder = uriBuilder.port(serverPort);
        }
        if (!(contextPath == null || contextPath.isEmpty())) {
            uriBuilder = uriBuilder.path(contextPath);
        }

        return uriBuilder;
    }

    public static String buildURI(HttpServletRequest request, String... pathSegments) {
        return buildURI2(request).pathSegment(pathSegments).build().normalize().toString();
    }

}
