package com.cydeo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
/**
 * Utility class for handling JWT (JSON Web Token) operations.
 *
 * JWT has three main component. "Header" has a <b>token type (JWT)</b> and <b>signIn algorithm</b> that is used (HS256)
 *
 * "Payload" has <b>claims</b>. Claims are the state of an entity (actual data).
 *
 * "Verify Signature" ensures that message it not changed along the process
 */
@Service
public class JwtService {

  /**
   * SignIn key for JWT token
   */
  @Value("${jwt.secret}")
  private String SECRET_KEY;

  /**
   * extract email from JWT. Subject is the e-mail of the user
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extract specific claim from JWT
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * creates token from user
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  /**
   * creates token from claims and user
   */
  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis())) // when this token created
        .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 30)) //how long this token should be valid (30m)
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals( userDetails.getUsername()) ) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts the claims (payload) from a JWT token.
   *
   * @param token The JWT token as a string.
   * @return A Claims object containing the token's claims.
   */
  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Retrieves the signing key from the JWT
   * @return A Key object representing the signing key.
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}