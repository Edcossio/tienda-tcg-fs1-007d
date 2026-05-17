package com.tiendatcg.ms_auth.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    
    private static final String SECRET_KEY = "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIGFuZCBsb25nIHNlY3JldCBrZXkgZm9yIEpXVCBzaWduYXR1cmU="; // Equivale
                                                                                                                                 // a
                                                                                                                                 // una
                                                                                                                                 // frase
                                                                                                                                 // secreta
                                                                                                                                 // muy
                                                                                                                                 // larga
                                                                                                                                 // en
                                                                                                                                 // Base64

    //Extraer el nombre de usuario del token
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // 2. Extraer un dato específico del token
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    // 3. Generar token solo con el username (Para usar en tu AutenticacionService)
    public String generarToken(String username) {
        return generarToken(new HashMap<>(), username);
    }

    // 4. Generar token con Claims extra 
    public String generarToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas
                .signWith(getSignInKey()) 
                .compact();
    }

    // 5. Validar si el token pertenece al usuario y aun no ha expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 6. Verificar si el token ya caduco
    private boolean isTokenExpired(String token) {
        return extraerExpiration(token).before(new Date());
    }

    // 7. Extraer la fecha de expiracion 
    private Date extraerExpiration(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    // 8. Desencriptar el token completo para leer su contenido
    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignInKey()) // Se usa verifyWith en lugar de setSigningKey
                .build()
                .parseSignedClaims(token) // Se usa parseSignedClaims en lugar de parseClaimsJws
                .getPayload(); // Se usa getPayload en lugar de getBody
    }

    // 9. Obtener la llave criptografica a partir de nuestro SECRET_KEY
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}