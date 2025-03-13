package unipay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {

    // HS512 algoritması için en az 512 bit (64 karakter) uzunluğunda gizli anahtar
    private final String SECRET_KEY = "F0yWjjjb6d9oY1C2M0XqXsrvLBGJyQq7ahXWqoc9n2WYrET1EFQET6eUo4u3f7vR";

    // Token geçerlilik süresi (örneğin 5 saat)
    private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    // Kullanıcı adına göre token oluşturma
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }

    // Token içerisinden kullanıcı adını çıkarma
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token içerisinden son kullanma tarihini çıkarma
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Token içerisindeki herhangi bir claim'i çıkarmak için genel metot
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Token’ın süresinin dolup dolmadığını kontrol etme
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token geçerliliğini, token içindeki kullanıcı adı ile sağlanan kullanıcı adını karşılaştırarak doğrulama
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
