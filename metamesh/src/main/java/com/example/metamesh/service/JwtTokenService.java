package com.example.metamesh.service;

import com.example.metamesh.dao.UserDao;
import com.example.metamesh.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

import static com.example.metamesh.config.DateConfig.newDate;
import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class JwtTokenService {
    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long TOKEN_EXPIRATION_TIME = 864_000_000; // 10 jours
    private final HttpServletRequest request;
    private final UserDao userDao;

    public String generateToken(String userId) {
        Date now = newDate();
        Date expiryDate = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public User resolveTokenFromRequest() {
        String token = verifyTokenFormat();
        if (isNull(token)) {
            return null;
        }

        String userId = resolveIdFromToken(token);

        return userDao.findById(userId);
    }

    private String verifyTokenFormat() {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (isValidTokenFormat(token)) {
                return token;
            }
        }
        return null;
    }

    private String resolveIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private boolean isValidTokenFormat(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
        } catch (Exception exception) {
            return false;
        }

        return true;
    }
}
