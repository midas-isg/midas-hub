package edu.pitt.isg.midas.hub.user;

import com.auth0.spring.security.mvc.Auth0JWTToken;
import com.auth0.spring.security.mvc.Auth0UserDetails;
import com.auth0.web.Auth0User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
class UserController {
    @Autowired
    private UserRule rule;

    @ApiOperation(value = "admin-user-profile", nickname = "admin-profile", notes = "This endpoint is for debugging purposes only. User is required to have ISG_ADMIN role. Applications should call to the other corresponding endpoint.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "path", value = "User's ID", required = true, dataType = "String", defaultValue = "windowslive|f6a65d71dcc20008")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Auth0User.class)})
    @RequestMapping(value = "/admin/api/user-profile/{userId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Auth0User getUserProfileViaUserId(@PathVariable String userId) {
        try {
            return rule.getUserProfileByUserId(userId);
        } catch (Exception e){
            throw new UserNotFoundException(e);
        }
    }

    @ApiOperation(value = "user-profile", nickname = "profile", notes = "This endpoint requires API Token (JWT) as Bearer Authorization Request Header Field (see Authorization parameter).")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "path", value = "User's ID", required = true, dataType = "String", defaultValue = "windowslive|f6a65d71dcc20008"),
            @ApiImplicitParam(name="Authorization",value = "credentials in format of 'Bearer {JWT.token}'", required = true, dataType = "string", paramType ="header", defaultValue = "Bearer Replace.this.with.your.JWT.API.Token.Keep.the.Bearer.as.is")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Auth0User.class),
            @ApiResponse(code = 500, message = "Failure")})
    @RequestMapping(value = "/api/user-profile/{userId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Auth0User getUserProfileViaUserIdRequiringAuthorization(
            @PathVariable String userId,
            @ApiIgnore @AuthenticationPrincipal Auth0JWTToken authenticationToken) {
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