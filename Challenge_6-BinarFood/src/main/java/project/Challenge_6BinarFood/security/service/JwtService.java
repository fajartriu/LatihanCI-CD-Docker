package project.Challenge_6BinarFood.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${binarfood.app.jwtSecret}")
    private String jwtSecret;

    public String extractUsername(String token) { //manipulate JWT tokens generate one information, validate token
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){ //extract single claim
        final Claims claims = extractAllClaims(token); // extract all claims
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){ // generate token without claims
        return buildToken(new HashMap<>(), userDetails);
    }

    public String buildToken( // generate token with claims
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 24)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){ //checking token still valid
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) { //checking token expire before today
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) { //extract expire from token -> claims method
        return extractClaim(token, Claims::getExpiration);
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
    } // decode key

    private Claims extractAllClaims(String token){ //extract all claims
        return Jwts.parser()
                .setSigningKey(key()) //secret that is used for digitally sign JWT
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
