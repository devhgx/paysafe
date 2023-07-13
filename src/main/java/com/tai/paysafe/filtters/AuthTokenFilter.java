package com.tai.paysafe.filtters;

import com.tai.paysafe.constants.JwtHeader;
import com.tai.paysafe.entities.RefreshToken;
import com.tai.paysafe.service.RefreshTokenService;
import com.tai.paysafe.service.impl.UserDetailsServiceImpl;
import com.tai.paysafe.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            String rft = parseRft(request);
            Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(rft);


            if(refreshTokenOpt.isPresent()){
                RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOpt.get());
            }

            if ((jwt != null && jwtUtil.validateJwtToken(jwt)) ) {
                Long userId = jwtUtil.getUserIdFromJwtToken(jwt);
                if (userId == 0) {
                    log.error("AuthTokenFilter cannot found userid in token");
                    throw new Exception("AuthTokenFilter cannot found userid in token");
                }
                Claims claims = jwtUtil.extractJwtData(jwt);
                String role = (String)claims.get("role");
                UserDetails userDetails = userDetailsService.loadUserByUserId(userId);
                if (!userDetails.getAuthorities().stream().anyMatch(x->x.getAuthority().equals(role))) {
                    log.error("AuthTokenFilter invalid role");
                    throw new Exception("AuthTokenFilter invalid role");
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(JwtHeader.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(JwtHeader.BEARER)) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
    private String parseRft(HttpServletRequest request) {
        String headerRft = request.getHeader(JwtHeader.REFRESH_TOKEN);
        if (StringUtils.hasText(headerRft)) {
            return headerRft;
        }

        return null;
    }

}