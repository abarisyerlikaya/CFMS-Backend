package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.LoginRequest;
import tr.edu.yildiz.cfms.api.models.LoginResponseData;
import tr.edu.yildiz.cfms.api.util.JwtUtil;
import tr.edu.yildiz.cfms.business.abstracts.AuthService;
import tr.edu.yildiz.cfms.business.abstracts.UserService;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessDataResponse;

@Service
public class AuthManager implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public Response login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            var authenticateRequest = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticateRequest);
            var userDetails = userService.loadUserByUsername(username);
            var data = new LoginResponseData(jwtUtil.generateToken(userDetails));
            return new SuccessDataResponse<>(data);
        } catch (BadCredentialsException e) {
            return new Response(false, HttpStatus.UNAUTHORIZED, "Incorrect username or password!");
        } catch (Exception e) {
            return new Response(false, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
        }


    }
}
