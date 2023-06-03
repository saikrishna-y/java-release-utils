package com.retailinsights.jwt.utils;

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


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenInfo {

    private String auditor;
    private String role;
    private List<String> authorities;

}
