package unipay.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.info("JWTAuthenticationFilter - doFilterInternal() START. Request URI: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                logger.info("JWT token detected. Extracting username...");
                username = jwtUtil.extractUsername(token);
                logger.info("Username extracted: {}", username);
            } catch (Exception e) {
                logger.error("Error while extracting username from token: {}", e.getMessage(), e);
                // Burada isterseniz response'a 401 veya 403 gönderebilirsiniz
                // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            }
        } else {
            logger.info("No valid Authorization header found (or doesn't start with 'Bearer ')");
        }

        // SecurityContext'te henüz auth yoksa ve username mevcutsa
        if (username != null && org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Loading user details from DB for username: {}", username);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            logger.info("Validating token...");
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                logger.info("Token is valid. Setting security context for user: {}", username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("Token validation failed for user: {}", username);
                // Burada opsiyonel olarak response'a hata fırlatılabilir
                // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation failed");
            }
        }

        // Devam et
        filterChain.doFilter(request, response);

        logger.info("JWTAuthenticationFilter - doFilterInternal() END.");
    }
}
