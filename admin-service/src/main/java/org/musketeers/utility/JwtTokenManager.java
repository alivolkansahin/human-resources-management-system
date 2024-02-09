//package org.musketeers.utility;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.musketeers.exception.ErrorType;
//import org.musketeers.exception.AdminServiceException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.Optional;
//
//@Component
//public class JwtTokenManager {
//
//    @Value("${admin-service-config.jwt.issuer}")
//    String issuer;
//
//    @Value("${admin-service-config.jwt.secret-key}")
//    String secretKey;
//
//    Long expTime = 1000L * 60 * 15;
//
//    public Optional<String> createToken(String adminId){
//
//        try {
//            return Optional.of(JWT.create().withAudience()
//                    .withClaim("adminId",adminId)
//                    .withIssuer(issuer)
//                    .withIssuedAt(new Date(System.currentTimeMillis()))
//                    .withExpiresAt(new Date(System.currentTimeMillis()+expTime))
//                    .sign(Algorithm.HMAC512(secretKey)));
//        } catch (Exception e) {
//            return Optional.empty();
//        }
//    }
//
//    public Optional<String> getIdFromToken(String token){
//        DecodedJWT decodedJWT = decodeToken(token);
//        if (decodedJWT == null) return Optional.empty();
//        String authId = decodedJWT.getClaim("authid").asString();
//        return Optional.of(authId);
//    }
//
//    public Optional<String> getRoleFromToken(String token){
//        DecodedJWT decodedJWT = decodeToken(token);
//        if (decodedJWT == null) return Optional.empty();
//        String role = decodedJWT.getClaim("role").asString();
//        return Optional.of(role);
//    }
//
//    private DecodedJWT decodeToken(String token) {
//        Algorithm algorithm = Algorithm.HMAC512(secretKey);
//        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
//        DecodedJWT decodedJWT = null;
//        try {
//            decodedJWT = verifier.verify(token);
//            return decodedJWT;
//        } catch (JWTVerificationException e) {
//            throw new AdminServiceException(ErrorType.INVALID_TOKEN);
//        }
//    }
//
//}