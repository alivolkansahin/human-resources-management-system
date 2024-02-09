package org.musketeers.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenManager {
    @Value("secret-key")
    String secretKey;
    @Value("issuer}")
    String issuer;
    Long expTime = 1000L*60*15; // 15dk
    // 1. Generate
    public Optional<String> createToken(String companyName){

        try {
            return Optional.of(JWT.create().withAudience()
                    .withClaim("companyName",companyName)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis()+expTime))
                    .sign(Algorithm.HMAC512(secretKey)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    // 2. Verify
    public Boolean verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT==null)
                return false;
        } catch (Exception e){
            return false;
        }
        return true;
    }
    // 3. Decode
    public Optional<String> decodeToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT==null)
                return Optional.empty();
            String companyName = decodedJWT.getClaim("companyName").asString();
            return Optional.of(companyName);
        } catch (Exception e){
            return Optional.empty();
        }
    }
}
