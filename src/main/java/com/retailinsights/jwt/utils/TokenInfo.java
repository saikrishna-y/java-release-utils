package com.retailinsights.jwt.utils;


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