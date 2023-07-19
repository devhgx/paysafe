package com.tai.paysafe.utils;

import com.tai.paysafe.constants.JwtHeader;
import com.tai.paysafe.dto.JwtDetails;
import com.tai.paysafe.dto.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class JwtUtil {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    @Lazy
    private HttpServletRequest request;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(item -> item.getAuthority()).toList();
        return generateToken(userPrincipal.getId().toString(), String.join(",", roles ));
    }

    public Long getUserIdFromJwtToken(String token) {
        String userIdStr = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return StringUtils.isNotEmpty(userIdStr)?Long.parseLong(userIdStr): 0;
    }

    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .addClaims(Map.of("role", role))
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            jwtParser().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
    public JwtParser jwtParser() {
        return Jwts.parser()
                .setSigningKey(jwtSecret);

    }

    public Claims extractJwtData(String jwtToken) {
        Jwt<?, ?> jwt = jwtParser().parseClaimsJws(jwtToken);
        return (Claims) jwt.getBody();
    }

    public JwtDetails getClaims() {
        var accessToken = request.getHeader(JwtHeader.AUTHORIZATION);
        String jwtToken = accessToken.substring("Bearer ".length());
        Claims claims = extractJwtData(jwtToken);
        Long userId = Long.parseLong(claims.getSubject());
        var jwt = new JwtDetails();
        jwt.setUserId(userId);
        jwt.setRole((String) claims.get("role"));
        return jwt;
    }

}