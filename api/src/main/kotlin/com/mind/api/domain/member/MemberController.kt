package com.mind.api.domain.member

import com.mind.api.common.dto.ResponseData
import com.mind.core.domain.member.MemberRequest
import com.mind.core.domain.member.MemberResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "회원 관리")
class MemberController(
    private val memberService: MemberService
) {

}
