MIDAS Commons
-------------
This project is the centralized web services that can be used by other MIDAS-Network applications.
It is a Java web application based on Spring Boot, Spring Security, Thymeleaf, Maven, and use Auth0 (http://auth0.com). 

------------------------------------------------------------------------
Instructions
------------

### Development
#### Downloading source code
```sh
$ git clone https://github.com/midas-isg/midas-hub
```

#### Run application
Run Application.main just as a Java application.

#### Current implementation
The overview of the protocol defined by Auth0 in https://auth0.com/docs/protocols#oauth-server-side is shown in the below picture: 

![alt tag](https://docs.google.com/drawings/d/1RZYKxbO5LM3fBhL8hs5-wefUkwDgPo-lOuoHWBdc0RI/pub?w=793&h=437)

Here is how current implementation according to the above picture (assuming deploying the app with /dev as context and 9000 as port):
1. authorize: The Auth0 lock.js (https://github.com/auth0/lock) in auth0Login.html prompts user to sign in. After the user chooses identity provider (e.g. Google, Facebook), the lock.js sends authorize request to Auth0.
2. Authenticate: Auth0, in turn, redirect the user to the identity provider to authenticate the user. This step may be skipped if the user already signed in with the provider.
3. code: If the user is authenticated, Auth0 will redirect to the URL (http://localhost:9000/dev/callback) provided as the callbackURL field in auth0Login.html with code and state as query parameters.
4. code: The doGet method of the configured Servlet Auth0ServletCallback (com.auth0::auth0-servlet) handles the redirection.
5. Get Access Token: The method fetches token and user profile from Auth0 and stores them into the HTTP Session.
Eventually, the browser gets redirected to the configured URL (http://localhost:9000/dev/auth0) by the redirectCallback Bean handled by method processAuth0Login of class Auth0LoginController.
The method saves user profile as Auth0User. This Auth0User is used as ModelAttribute in other Controllers. Finally, it redirects to http://localhost:9000/dev/terms.

------------------------------------------------------------------------
### Deployment
#### Start
1. Download source code, See [Downloading Source code](#downloading-source-code).
2. Run `./start.sh`.
3. If this is first run, it will fail because the previous step force you to override properties.
4. Edit `config/application.properties` file to match auth0.com and your application settings.
5. Run `./start.sh` again.

------------------------------------------------------------------------
