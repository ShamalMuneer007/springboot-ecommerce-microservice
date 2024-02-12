package org.shamal.identityservice.services.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.shamal.identityservice.config.CloudConfig;
import org.shamal.identityservice.repositories.UserIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class JwtService {
    @Autowired
    private CloudConfig cloudConfig;
    @Autowired
    private UserIdentityRepository userIdentityRepository;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        UUID userId = userIdentityRepository.findByUsername(username).getIdentity_id();
        Date expirationTime = new Date(currentDate.getTime() + cloudConfig.getJwtExpiration());
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", roles.get(0))
                .claim("userId",userId)
                .setIssuedAt(currentDate)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, cloudConfig.getJwtSecret())
                .compact();
    }
    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(cloudConfig.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
        return  claims.getSubject();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(cloudConfig.getJwtSecret()).parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException e){
            throw new AuthenticationCredentialsNotFoundException("Jwt token is expired");
        }
        catch (InvalidClaimException e){
            throw new AuthenticationCredentialsNotFoundException("Jwt token is invalid");
        }
        catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Something is wrong with the jwt token");
        }

    }
    public void expireToken(String token){
        Date currentDate = new Date();
        Claims claims = Jwts.parser()
                .setSigningKey(cloudConfig.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
        claims.setExpiration(new Date(currentDate.getTime()));
    }
    public String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
