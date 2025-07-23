package com.example.ems.security;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        var header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            var token = header.substring(7);
            try {
                var claims = Jwts.parser()
                                 .setSigningKey(jwtSecret)
                                 .parseClaimsJws(token)
                                 .getBody();

                var username = claims.getSubject();
                var userId = claims.get("userId", String.class);
                var permissions = (List<String>) claims.get("permissions", List.class);

                var authorities = new HashSet<SimpleGrantedAuthority>();
                if (permissions != null) {
                    for (String perm : permissions) {
                        authorities.add(new SimpleGrantedAuthority(perm.toUpperCase()));
                    }
                }

                var group = claims.get("group", String.class);
                if (group != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + group));
                }

                var authenticatedUser = new AuthenticatedUser(userId, username, authorities);

                var authentication =
                        new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);

                SecurityContextHolder.getContext()
                                     .setAuthentication(authentication);
            } catch (Exception e) {
                log.warn(e.getMessage());
                response.sendError(SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}