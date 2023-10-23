package com.retailinsights.jwt.handler;

/*-
 * *****
 * jwt-utils
 * -------
 * Copyright (C) 2021 - 2023 RetailInsights
 * -------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ======
 */


import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.retailinsights.jwt.service.JwtService;
import com.retailinsights.jwt.utils.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.require;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.retailinsights.jwt.utils.JwtConstants.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofMinutes;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class JwtHandler implements JwtService {

    @Value("${secret:zx@#$OP(@#}")
    private String secret;

    @Value("${issuer:https://theretailinsights.com/}")
    private String issuer;

    @Value("${jwt.token.access.expiration:10}")
    private Integer accessTokenExpTime;

    @Value("${jwt.token.refresh.expiration:10}")
    private Integer refreshTokenExpTime;


    @Override
    public Function<Map<String, Object>, String> signAccessToken() {
        return payloadMap ->
                create()
                        .withExpiresAt(now().plus(ofMinutes(accessTokenExpTime)))
                        .withPayload(payloadMap)
                        .withIssuer(issuer)
                        .sign(HMAC512(secret));
    }

    @Override
    public Function<String, String> signRefreshToken() {
        return auditor ->
                create()
                        .withSubject(auditor)
                        .withClaim(AXIS_ROTATION_TYPE, AXIS)
                        .withExpiresAt(now().plus(ofMinutes(refreshTokenExpTime)))
                        .withIssuer(issuer)
                        .sign(HMAC512(secret));
    }

    @Override
    public TokenInfo getTokenInfo(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        Map<String, Object> permissionsMap = decodedJWT.getClaim(PERMISSIONS_KEY).asMap();
        return TokenInfo.builder().auditor(decodedJWT.getSubject())
                .role(decodedJWT.getClaim(ROLE_KEY).asString())
                .authorities(permissionsMap.entrySet()
                        .stream()
                        .map(map -> String.join(" ", map.getKey(), String.valueOf(map.getValue())))
                        .toList()).build();
    }

    @Override
   public Map<String,Claim> getClaims(String token){
      return decodedToken(token).getClaims();
   }

    @Override
    public String getAuditor(String token) {
        return this.decodedToken(token).getSubject();
    }

    private DecodedJWT decodedToken(String token) {
        return getAlgorithm()
                .andThen(algorithm -> require(algorithm).withIssuer(issuer).acceptExpiresAt(MINUTES.toMinutes(accessTokenExpTime)).build())
                .andThen(jwtVerifier -> jwtVerifier.verify(token))
                .apply(secret);//function starts here
    }

    private static Function<String, Algorithm> getAlgorithm() {
        return secret -> HMAC512(secret.getBytes(UTF_8));
    }

}
