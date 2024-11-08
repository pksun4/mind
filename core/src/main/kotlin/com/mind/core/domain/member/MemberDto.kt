package com.mind.core.domain.member

import com.mind.core.enums.GenderEnums
import java.time.LocalDateTime

class MemberRequest(
    val email: String,
    val password: String,
    val name: String,
    val gender: GenderEnums
)

class MemberResponse(
    val id: Long,
    val email: String,
    val name: String,
    val gender: GenderEnums,
    val createdAt: LocalDateTime
) {
    companion object {
        operator fun invoke(member: Member) = MemberResponse(
            id = member.id!!,
            email = member.email,
            name = member.name,
            gender = member.gender,
            createdAt = member.createdAt!!
        )
    }
}
