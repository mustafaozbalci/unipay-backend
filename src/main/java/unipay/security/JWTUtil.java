package unipay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for generating and validating JWT tokens.
 * Uses HS512 signature algorithm and a secret key.
 */
@Component
public class JWTUtil {

    /**
     * Secret key for signing JWTs (must be at least 512 bits for HS512).
     */
    private final String SECRET_KEY = "F0yWjjjb6d9oY1C2M0XqXsrvLBGJyQq7ahXWqoc9n2WYrET1EFQET6eUo4u3f7vR";

    /**
     * Token validity duration in milliseconds (e.g., 5 hours).
     */
    private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    /**
     * Generates a JWT token containing the username as subject,
     * with issued and expiration timestamps.
     *
     * @param username the username to include in the token subject
     * @return the signed JWT token string
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);

        return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()).compact();
    }

    /**
     * Extracts the username (subject) from the given token.
     *
     * @param token the JWT token string
     * @return the username stored in token subject
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given token.
     *
     * @param token the JWT token string
     * @return the expiration timestamp
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the token by applying the provided resolver.
     *
     * @param token          the JWT token string
     * @param claimsResolver function to retrieve a value from Claims
     * @param <T>            the type of the claim value
     * @return the extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    /**
     * Checks whether the token has expired.
     *
     * @param token the JWT token string
     * @return true if current time is after token expiration
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the token by comparing its subject to the provided username
     * and ensuring it has not expired.
     *
     * @param token    the JWT token string
     * @param username the username to match against the token subject
     * @return true if token is valid and belongs to the specified user
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }
}
