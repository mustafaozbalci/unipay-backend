package unipay.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that intercepts HTTP requests to validate JWT tokens.
 * If a valid Bearer token is present, the user is authenticated in Spring Security context.
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Inspect the Authorization header for a Bearer token, validate it,
     * and set the authentication in the SecurityContext if valid.
     *
     * @param request     incoming HTTP servlet request
     * @param response    HTTP servlet response
     * @param filterChain chain of filters to continue processing
     * @throws ServletException in case of servlet errors
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        // Check for Bearer token in Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = null;

            try {
                // Extract username from the token
                username = jwtUtil.extractUsername(token);
            } catch (Exception ex) {
                logger.warn("Failed to extract username from JWT for {}: {}", uri, ex.getMessage());
            }

            // Proceed if username is obtained and user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // Validate token against userDetails
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    // Build authentication object and set in context
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    logger.debug("Authenticated '{}' for {}", username, uri);
                } else {
                    logger.warn("Invalid JWT token for '{}' on {}", username, uri);
                }
            }
        } else {
            logger.debug("No Bearer token provided for {}", uri);
        }

        // Continue filter chain regardless of authentication outcome
        filterChain.doFilter(request, response);
    }
}
