package tr.edu.yildiz.cfms.api.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tr.edu.yildiz.cfms.api.util.JwtUtil;
import tr.edu.yildiz.cfms.business.abstracts.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String BEARER = "Bearer ";
    private static final int BEARER_LENGTH = BEARER.length();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String jwt = authorizationHeader.startsWith(BEARER) ? authorizationHeader.substring(BEARER_LENGTH) : authorizationHeader;
        String username = jwtUtil.extractUsername(jwt);

        if (username == null || username.isEmpty() || SecurityContextHolder.getContext().getAuthentication() != null)
            return;

        var user = userService.getByUsername(username);
        var userDetails = userService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(jwt, user))
            return;

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
        var details = new WebAuthenticationDetailsSource().buildDetails(request);
        usernamePasswordAuthenticationToken.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean result = path.startsWith("/api/webhooks") ||
                path.startsWith("/api/login") ||
                path.startsWith("/chat") ||
                path.startsWith("/send-message") ||
                path.startsWith("/create-conversation") ||
                path.startsWith("/topic") ||
                path.startsWith("/api/cloud-balancing");
        return result;
    }
}
