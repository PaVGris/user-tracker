package com.diplom.analytics.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String url;
}