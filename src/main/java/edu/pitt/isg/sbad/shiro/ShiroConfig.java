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
package com.example.shiro;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * May 14, 2015 1:53:28 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "passwordService")
    public DefaultPasswordService passwordService() {
        return new DefaultPasswordService();
    }

    @Bean(name = "credentialsMatcher")
    public PasswordMatcher credentialsMatcher() {
        final PasswordMatcher credentialsMatcher = new PasswordMatcher();
        credentialsMatcher.setPasswordService(passwordService());

        return credentialsMatcher;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
/*
    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public CCDAuthorizingRealm realm() {
        CCDAuthorizingRealm realm = new CCDAuthorizingRealm();
        realm.setCredentialsMatcher(credentialsMatcher());

        return realm;
    }*/

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //securityManager.setRealm(realm());

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager());
        factoryBean.setLoginUrl("/login");
        factoryBean.setUnauthorizedUrl("/401");
        factoryBean.setSuccessUrl("/secured/ping");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/secured/**", "authc");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

}
