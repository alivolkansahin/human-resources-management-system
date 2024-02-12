package org.musketeers.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.musketeers.entity.enums.ERole;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenManager {
    @Value("${authserviceconfig.secrets.secret-key}")
    String secretKey;

    @Value("${authserviceconfig.secrets.issuer}")
    String issuer;
    Long expTime=1000L*60*90;

    public Optional<String> createToken(String id, ERole role){
        try {
          return Optional.of(JWT.create()
                    .withAudience()
                    .withClaim("id", id)
                    .withClaim("role",role.toString())
                    .withIssuer(issuer)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + expTime))
                    .sign(Algorithm.HMAC512(secretKey)));
        }
        catch (Exception e){
            return Optional.empty();
        }

    }

    /* Volkan:
    artık bi yerde tokeni deşifre ederken aşağıdaki 3 yöntem gibi alacağız. Eğer bir yerde hem id hem rolü kontrol etmek gerekiyorsa (ki olacak) diye bu hale getirdim.
    2 yerde kontrol etmek gerekiyorsa 1 methodda, 2 defa jwtTokenManagerin getclaimsfromtoken methodunu çalıştırmaya gerek yok artık, 1 kere çalışsın hepsini alsın (3.yöntemdeki gibi)
                        1.yöntem --->  String id = jwtTokenManager.getClaimsFromToken(token).get(0);     --> burdaki id authid olacak her zaman. Diğer servisler karıştırmamalı bunu.
                        2.yöntem --->  String role = jwtTokenManager.getClaimsFromToken(token).get(1);   --> Bu tokene sahip olan kişi kim, admin mi supervisor mı guest mi onun kontrolü
                        3.yöntem --->  List<String> claims = jwtTokenManager.getClaimsFromToken(token);
    Bütün servislerin decode kısmı aynı oldu, uyumlu oldu. Create kısmı sende ve seninle konuşuruz onu :D
     */
    public List<String> getClaimsFromToken(String token){
        DecodedJWT decodedJWT = decodeToken(token);
        Optional<String> optionalId = Optional.ofNullable(decodedJWT.getClaim("id").asString());
        Optional<String> optionalRole = Optional.ofNullable(decodedJWT.getClaim("role").asString());
        if(optionalId.isEmpty() || optionalRole.isEmpty()) throw new AuthServiceException(ErrorType.INVALID_TOKEN);
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
            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
        }
    }
}
