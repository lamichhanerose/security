package com.example.security.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.security.user.Customer;
import com.example.security.user.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRECT_KEY = "556B58703272357538782F413F4428472B4B6250655368566D59713374367639";

    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);
    }
    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extracctAllClaims(token);
        return  claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }
    public String generateTokenForCustomer(Customer customer){
        return generateTokenForCustomer(new HashMap<>() , customer);
    }

    public String generateToken(
        Map<String , Object> extraClaims,
        UserDetails userDetails
    ){
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000  * 60 * 24))
            .signWith(getSignInKey() , SignatureAlgorithm.HS256  )
            .compact();
    }
    public String generateTokenForCustomer(
        Map<String , Object> extraClaims,
        Customer customer
    ){
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(customer.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000  * 60 * 24))
            .signWith(getSignInKey() , SignatureAlgorithm.HS256  )
            .compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }

    private Claims extracctAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRECT_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
    }
}
