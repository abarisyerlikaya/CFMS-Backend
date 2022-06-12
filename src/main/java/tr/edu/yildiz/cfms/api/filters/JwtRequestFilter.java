package tr.edu.yildiz.cfms.api.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.edu.yildiz.cfms.api.controllers.AuthController;
import tr.edu.yildiz.cfms.api.models.LoginRequest;
import tr.edu.yildiz.cfms.api.models.LoginResponseData;
import tr.edu.yildiz.cfms.api.util.JwtUtil;
import tr.edu.yildiz.cfms.business.abstracts.AuthService;
import tr.edu.yildiz.cfms.business.abstracts.UserService;
import tr.edu.yildiz.cfms.core.response_types.Response;
import tr.edu.yildiz.cfms.core.response_types.SuccessDataResponse;
import tr.edu.yildiz.cfms.core.utils.ExternalApiClients;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static tr.edu.yildiz.cfms.core.utils.Constants.*;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;

        if (request.getRequestURI().startsWith("/api/webhooks"))
            jwt = ADMIN_JWT;
        else if (authorizationHeader != null && !authorizationHeader.isEmpty())
            jwt = authorizationHeader.startsWith(BEARER) ? authorizationHeader.substring(BEARER_LENGTH) : authorizationHeader;
        else return;

        String username = jwtUtil.extractUsername(jwt);

        if (username == null || username.isEmpty() || SecurityContextHolder.getContext().getAuthentication() != null)
            return;

        var user = userService.getByUsername(username);
        var userDetails = userService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(jwt, user))
            return;

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        var details = new WebAuthenticationDetailsSource().buildDetails(request);
        usernamePasswordAuthenticationToken.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean result = path.startsWith("/api/login") ||
                path.startsWith("/chat") ||
                path.startsWith("/send-message") ||
                path.startsWith("/create-conversation") ||
                path.startsWith("/topic") ||
                path.startsWith("/api/optimization");
        return result;
    }
}
