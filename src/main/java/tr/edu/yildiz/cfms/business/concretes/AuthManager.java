package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.LoginRequest;
import tr.edu.yildiz.cfms.api.models.LoginResponseData;
import tr.edu.yildiz.cfms.api.util.JwtUtil;
import tr.edu.yildiz.cfms.business.abstracts.AuthService;
import tr.edu.yildiz.cfms.business.abstracts.UserService;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessDataResponse;
import tr.edu.yildiz.cfms.core.response_types.SuccessResponse;
import static tr.edu.yildiz.cfms.core.utils.Constants.BEARER;
import static tr.edu.yildiz.cfms.core.utils.Constants.BEARER_LENGTH;

@Service
public class AuthManager implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private JwtUtil jwtUtil;

    public Response login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            var authenticateRequest = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticateRequest);
            var user = userService.getByUsername(username);
            if (!user.getPassword().equals(password))
                throw new BadCredentialsException("Incorrect username or password!");
            var data = new LoginResponseData(jwtUtil.generateToken(user));
            return new SuccessDataResponse<>(data);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return new Response(false, HttpStatus.UNAUTHORIZED, "Incorrect username or password!");
        } catch (Exception e) {
            return new Response(false, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
        }
    }

    @Override
    public Response logout(String token) {
        try {
            if (token.startsWith(BEARER)) token = token.substring(BEARER_LENGTH);
            String username = jwtUtil.extractUsername(token);
            for (Object principal : sessionRegistry.getAllPrincipals()) {
                if (principal instanceof UserDetails) {
                    var userDetails = (UserDetails) principal;
                    if (userDetails.getUsername().equals(username))
                        for (SessionInformation information : sessionRegistry.getAllSessions(userDetails, false)) {
                            information.expireNow();
                            sessionRegistry.removeSessionInformation(information.getSessionId());
                        }


                }
            }
            return new SuccessResponse("Successfully logged out!");
        } catch (Exception e) {
            return new Response(false, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
        }
    }

}
