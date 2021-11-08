package com.example.myjwt.filter.jwt;

public interface JwtProperties
{
    String SECRET = "PRIVATE_KEY";
    int EXPIRATION_TIME = 60000*10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
