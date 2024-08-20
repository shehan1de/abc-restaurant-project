package com.abcRestaurant.abcRestaurant.Security;

import com.abcRestaurant.abcRestaurant.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Use a Base64 encoded key string for the secret key
    private final Key secretKey = Keys.hmacShaKeyFor(
            java.util.Base64.getDecoder().decode("wGVjqG9WAGPaebFI42cxGx6AY8VYRiYsGegRc3N6Gdk=")
    );
    private final long jwtExpirationInMs = 86400000; // Example: 1 day

    // Generate JWT token with user details
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("userType", user.getUserType());
        claims.put("userEmail", user.getUserEmail());
        claims.put("profilePicture", user.getProfilePicture());
        return createToken(claims, user.getUsername());
    }

    // Create JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String usernameFromToken = extractUsername(token);
        return (usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract specific claim from JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from JWT token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    // Check if JWT token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractProfilePicture(String token) {
        return extractClaim(token, claims -> claims.get("profilePicture", String.class));
    }
}
