spring-boot-auth0-demo
======================

Spring Boot Auth0 Demo
----------------------
This project is a Java web application based on Spring Boot, Thymeleaf and Shiro as a demo how to use Auth0 (http://auth0.com). 

------------------------------------------------------------------------
Instructions
------------

### Development
#### Downloading source code
```sh
$ git clone https://git.isg.pitt.edu/saw111/spring-boot-auth0-demo
```

#### Run application
Run Application.main just as a Java application.

#### Current implementation
The overview of the protocol defined by Auth0 in https://auth0.com/docs/protocols#oauth-server-side is shown in the below picture: 

![alt tag](https://docs.google.com/drawings/d/1RZYKxbO5LM3fBhL8hs5-wefUkwDgPo-lOuoHWBdc0RI/pub?w=793&h=437)

Here is how current implementation according to the above picture:
1. authorize: The Auth0 lock.js (https://github.com/auth0/lock) in auth0Login.html prompts user to sign in. After the user chooses identity provider (e.g. Google, Facebook), the lock.js sends authorize request to Auth0.
2. Authenticate: Auth0, in turn, redirect the user to the identity provider to authenticate the user. This step may be skipped if the user already signed in with the provider.
3. code: If the user is authenticated, Auth0 will redirect to the URL (http://localhot:3001/callback) provided as the callbackURL field in auth0Login.html with code and state as query parameters.
4. code: The doGet method of the configured Servlet Auth0ServletCallback (com.auth0::auth0-servlet) handles the redirection.
5. Get Access Token: The method fetches token and user profile from Auth0 and stores them into the HTTP Session.
Eventually, the browser gets redirected to the configured URL (http://localhot:3001/auth0) by the redirectCallback Bean handled by method processAuth0Login of class Auth0LoginController.
The method converts saved user profile as Auth0User to be AppUser. This AppUser is used as ModelAttribute in other Controllers. Finally, it redirects to http://localhot:3001/secured/terms.

------------------------------------------------------------------------
### Deployment
#### Configuration
1. Download source code, See [Downloading Source code](#downloading-source-code).
2. Edit `src/main/resources/auth0.properties` file to match auth0.com application settings.

------------------------------------------------------------------------
