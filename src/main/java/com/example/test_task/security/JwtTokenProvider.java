package com.example.test_task.security;

import com.example.test_task.exception.AccessDeniedException;
import com.example.test_task.model.Role;
import com.example.test_task.model.User;
import com.example.test_task.security.props.JwtProperties;
import com.example.test_task.service.UserService;
import com.example.test_task.web.dto.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(UUID id, String login, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("id", id);
        claims.put("roles", resolveRoles(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(UUID id, String login) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("id", id);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        JwtResponse jwtResponse = JwtResponse.builder().build();
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied");
        }
        UUID id = UUID.fromString(getId(refreshToken));
        User user = userService.getUserById(id);
        jwtResponse.setId(id);
        jwtResponse.setLogin(user.getLogin());
        jwtResponse.setAccessToken(createAccessToken(id, user.getLogin(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(id, user.getLogin()));
        return jwtResponse;
    }

    public Authentication getAuthentication(String token) {
        String login = getLogin(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getRole)
                .collect(Collectors.toList());
    }

    private String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    private String getLogin(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}