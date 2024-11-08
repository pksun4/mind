package com.mind.api.domain.authentication

import com.mind.api.common.dto.ResponseData
import com.mind.api.domain.member.MemberService
import com.mind.api.security.TokenRequest
import com.mind.core.domain.member.MemberRequest
import com.mind.core.domain.member.MemberResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
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
    suspend fun signUp(memberRequest: MemberRequest) = memberService.signUp(memberRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(MemberResponse(it)) }
    )

    @PostMapping("/login")
    suspend fun login(tokenRequest: TokenRequest) = authenticationService.login(tokenRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(it) }
    )
}
