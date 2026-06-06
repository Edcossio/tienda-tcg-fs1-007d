package com.tiendatcg.api_gateway.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    // cambio 1
    private final String SECRET_KEY = System.getenv()
            .getOrDefault("JWT_SECRET",
                    "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIGFuZCBsb25nIHNlY3JldCBrZXkgZm9yIEpXVCBzaWduYXR1cmU=");

    public void validarToken(final String token) {

        Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token);
    }

    public String extraerRol(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("rol", String.class); // Extraemos la llave "rol" que guardamos en ms-auth
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extraerIdUsuario(String token) {
        Object id = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("idUsuarioRef");
        return id != null ? String.valueOf(id) : null;
    }
}