package com.mind.api.domain.authentication

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "토큰 요청")
class TokenRequest(

    @Schema(name = "이메일", example = "mind@gmail.com")
    @field:NotBlank(message = "이메일은 필수값입니다.")
    val email: String,

    @Schema(name = "비밀번호", example = "1234")
    @field:NotBlank(message = "비밀번호는 필수값입니다.")
    val password: String
)

@Schema(description = "토큰 응답")
class TokenResponse(
    @Schema(name = "토큰 타입")
    val grantType: String,
    @Schema(name = "액세스 토큰")
    val accessToken: String,
    @Schema(name = "리프레시 토큰")
    val refreshToken: String
)
