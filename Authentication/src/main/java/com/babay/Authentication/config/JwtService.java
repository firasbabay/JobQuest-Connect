package com.babay.Authentication.config;

import com.babay.Authentication.model.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import static com.babay.Authentication.model.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static com.babay.Authentication.model.Constants.SIGNING_KEY;


@Service
@RequiredArgsConstructor
public class JwtService {

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Account user) {
        return doGenerateToken(user);
    }

    private String doGenerateToken(Account user) {

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("authorities", new ArrayList<String>() {{add(user.getRole().name());}});

        String t = null;
        try{
            t = Jwts.builder()
                    .setClaims(claims)
                    .setIssuer("https://devglan.com")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                    .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                    .compact();
        } catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }
}
