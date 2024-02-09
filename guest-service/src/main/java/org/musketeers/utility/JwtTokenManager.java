package org.musketeers.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.GuestServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenManager {

    @Value("${guest-service-config.jwt.issuer}")
    String issuer;

    @Value("${guest-service-config.jwt.secret-key}")
    String secretKey;

    public List<String> getClaimsFromToken(String token){
        DecodedJWT decodedJWT = decodeToken(token);
        Optional<String> optionalId = Optional.ofNullable(decodedJWT.getClaim("id").asString());
        Optional<String> optionalRole = Optional.ofNullable(decodedJWT.getClaim("role").asString());
        if(optionalId.isEmpty() || optionalRole.isEmpty()) throw new GuestServiceException(ErrorType.INVALID_TOKEN);
        return Arrays.asList(optionalId.get(), optionalRole.get());
    }

    private DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException e) {
            throw new GuestServiceException(ErrorType.INVALID_TOKEN);
        }
    }

}