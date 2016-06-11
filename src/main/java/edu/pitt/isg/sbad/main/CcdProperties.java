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
package com.example.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * Feb 13, 2016 2:44:02 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
@Component
@PropertySource("classpath:ccd.properties")
public class CcdProperties {

    @Value("${ccd.callback.server.name}")
    private String callbackServerName;

    @Value("${ccd.callback.server.port}")
    private String callbackServerPort;



    public CcdProperties() {
    }

    public String getCallbackServerName() {
        return callbackServerName;
    }

    public void setCallbackServerName(String callbackServerName) {
        this.callbackServerName = callbackServerName;
    }

    public String getCallbackServerPort() {
        return callbackServerPort;
    }

    public void setCallbackServerPort(String callbackServerPort) {
        this.callbackServerPort = callbackServerPort;
    }
}
