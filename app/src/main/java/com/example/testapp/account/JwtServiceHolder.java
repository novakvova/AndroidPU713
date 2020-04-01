package com.example.testapp.account;

public interface JwtServiceHolder {
    void saveJWTToken(String token);
    String getToken();
    void removeToken();
}