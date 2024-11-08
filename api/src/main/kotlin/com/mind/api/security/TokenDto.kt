package com.mind.api.security

class TokenRequest(
    val email: String,
    val password: String
)

class TokenResponse(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)
