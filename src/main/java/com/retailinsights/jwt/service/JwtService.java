package com.retailinsights.jwt.service;

/*-
 * *****
 * jwt-utils
 * -------
 * Copyright (C) 2021 - 2023 RetailInsights
 * -------
 * This software is owned exclusively by RetailInsights.
 * As such, this software may not be copied, modified, or
 * distributed without permission from RetailInsights.
 * ======
 */

import com.retailinsights.jwt.utils.TokenInfo;

import java.util.Map;
import java.util.function.Function;

import static com.retailinsights.jwt.utils.JwtConstants.BEARER_KEY;

public interface JwtService {

    Function<Map<String, Object>, String> signAccessToken();

    Function<String, String> signRefreshToken();

    TokenInfo getTokenInfo(String token);

    String getAuditor(String token);

    default String trimToken(String token) {
        return token.substring(BEARER_KEY.length());
    }
}
