package com.example.testapp.account;

public interface JwtServiceHolder {
    void SaveJWTToken(String token);
    String getToken();
}