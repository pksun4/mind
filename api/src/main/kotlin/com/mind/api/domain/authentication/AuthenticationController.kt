package com.mind.api.domain.authentication

import com.mind.api.common.dto.ResponseData
import com.mind.api.domain.member.MemberRequest
import com.mind.api.domain.member.MemberResponse
import com.mind.api.domain.member.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/authentications")
@Tag(name = "인증 관리")
class AuthenticationController(
    private val memberService: MemberService,
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    fun signUp(@Valid @RequestBody memberRequest: MemberRequest) = memberService.signUp(memberRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(MemberResponse.make(it)) }
    )

    @PostMapping("/login")
    @Operation(summary = "로그인")
    fun login(@Valid @RequestBody tokenRequest: TokenRequest) = authenticationService.login(tokenRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(it) }
    )
}
