package edu.pitt.isg.midas.hub.user;

import com.auth0.spring.security.mvc.Auth0JWTToken;
import com.auth0.spring.security.mvc.Auth0UserDetails;
import com.auth0.web.Auth0User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {
    @Autowired
    private UserRule rule;

    @RequestMapping(value = "/public/api/user-profile/{userId}", method = RequestMethod.GET)
    public Auth0User getUserProfileViaUserId(@PathVariable String userId) {
        try {
            return rule.getUserProfileByUserId(userId);
        } catch (Exception e){
            throw new UserNotFoundException(e);
        }
    }

    @RequestMapping(value = "/api/user-profile/{userId}", method = RequestMethod.GET)
    public Auth0User getUserProfileViaUserIdRequiringAuthorization(@PathVariable String userId, Auth0JWTToken authenticationToken) {
        try {
            final Auth0UserDetails userDetails = (Auth0UserDetails)authenticationToken.getPrincipal();
            rule.guardAuthorization(userId, userDetails);
        } catch (Exception e){
            throw new UnauthorizedAccessException(e);
        }
        return getUserProfileViaUserId(userId);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    static class UnauthorizedAccessException extends RuntimeException {
        UnauthorizedAccessException(Throwable cause) {
            super(cause);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static class UserNotFoundException extends RuntimeException {
        UserNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}