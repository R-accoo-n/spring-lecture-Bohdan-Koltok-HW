package co.inventorsoft.academy.spring.security;

import co.inventorsoft.academy.spring.exceptions.AuthException;
import co.inventorsoft.academy.spring.exceptions.NotFoundException;
import co.inventorsoft.academy.spring.models.User;
import co.inventorsoft.academy.spring.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.token.secret}")
    private String secretKey = "secret";

    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("role",user.getUserRole());
        claims.put("username",user.getUsername());
        claims.put("notification_type",user.getNotificationType());
        Date tokenCreateTime = new Date();
        final long accessTokenValidity = 60L * 60L * 1000L;
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(
            accessTokenValidity));
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(tokenValidity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public String resolveToken(HttpServletRequest request) {

        final String TOKEN_HEADER = "Authorization";
        String bearerToken = request.getHeader(TOKEN_HEADER);
        final String TOKEN_PREFIX = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Optional<User> user = this.userRepository.findByEmail(getEmail(token));
        UserDetails userDetails;
        if (user.isPresent()) {
            userDetails = new JwtUser(user.get().getEmail(), user.get().getUserRole(),
                List.of(new SimpleGrantedAuthority(user.get().getUserRole().toString())));
        } else {
            throw new NotFoundException(NotFoundException.USER_EMAIL_NOT_FOUND);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException(AuthException.INVALID_AUTH_TOKEN);
        }
    }

}
