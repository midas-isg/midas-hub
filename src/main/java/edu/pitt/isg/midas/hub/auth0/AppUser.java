/*
 * Copyright (C) 2015 University of Pittsburgh.
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

import org.springframework.context.annotation.Scope;

import java.util.Date;

/**
 *
 * May 18, 2015 9:32:20 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
@Scope("session")
public class AppUser  {
    private String firstName;
    private String lastName;
    private String email;
    private Date lastLogin;
    private boolean localAccount;

    public String getName() {
        if (firstName == null)
            return getEmail();
        String name = firstName;
        if (lastName != null)
            name += " " + lastName;
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean getLocalAccount() {
        return localAccount;
    }

    public void setLocalAccount(boolean localAccount) {
        this.localAccount = localAccount;
    }

}
