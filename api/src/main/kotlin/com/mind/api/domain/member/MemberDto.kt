package com.mind.api.domain.member

import com.mind.api.security.SecurityUtils.passwordEncode
import com.mind.core.domain.member.Member
import com.mind.core.enums.GenderEnums
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "회원가입 요청")
class MemberRequest(
    @Schema(name = "이메일")
    val email: String,
    @Schema(name = "비밀번호")
    val password: String,
    @Schema(name = "이름")
    val name: String,
    @Schema(name = "성별")
    val gender: GenderEnums
) {
    companion object {
        fun convert(memberRequest: MemberRequest) = Member(
            email = memberRequest.email,
            password = memberRequest.password.passwordEncode(),
            name = memberRequest.name,
            gender = memberRequest.gender
        )
    }
}

@Schema(description = "회원가입 응답")
class MemberResponse(
    @Schema(name = "아이디")
    val id: Long,
    @Schema(name = "이메일")
    val email: String,
    @Schema(name = "이름")
    val name: String,
    @Schema(name = "성별")
    val gender: GenderEnums,
    @Schema(name = "등록일")
    val createdAt: LocalDateTime
) {
    companion object {
        fun make(member: Member) = MemberResponse(
            id = member.id!!,
            email = member.email,
            name = member.name,
            gender = member.gender,
            createdAt = member.createdAt!!
        )
    }
}

@Schema(description = "회원정보 수정")
class MemberUpdateRequest(
    @Schema(name = "기존 비밀번호")
    val currentPassword: String,
    @Schema(name = "변경 비밀번호")
    val changePassword: String,
)
