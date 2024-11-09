package com.mind.api.domain.member

import com.mind.api.common.dto.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "회원 관리")
class MemberController(
    private val memberService: MemberService
) {
    @PutMapping
    @Operation(summary = "회원 비밀번호 변경")
    fun updateMember(memberUpdateRequest: MemberUpdateRequest) = memberService.updateMember(memberUpdateRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(it) }
    )
}
