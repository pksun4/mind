package com.mind.api.domain.member

import arrow.core.left
import arrow.core.right
import com.mind.api.security.SecurityUtils.passwordEncode
import com.mind.core.domain.member.Member
import com.mind.core.domain.member.MemberRepository
import com.mind.core.domain.member.MemberRequest
import com.mind.core.domain.member.MemberRole
import com.mind.core.enums.ResponseEnums
import com.mind.core.enums.RoleEnums
import com.mind.core.util.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    @Transactional
    suspend fun signUp(memberRequest: MemberRequest) =
        runCatching {
            memberRepository.findByEmail(memberRequest.email)?.let {
                MemberError.MemberEmailDuplicated.left()
            } ?: memberRepository.save(
                Member(
                    email = memberRequest.email,
                    password = memberRequest.password.passwordEncode(),
                    name = memberRequest.name,
                    gender = memberRequest.gender
                ).apply {
                    this.memberRole = mutableListOf(
                        MemberRole.grantMember(this)
                    )
                }
            ).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
    private fun Throwable.throwUnknownError() = MemberError.Unknown(this.javaClass.name).left()
}

sealed class MemberError(
    val responseEnums: ResponseEnums
)  {
    data object MemberEmailDuplicated: MemberError(ResponseEnums.MEMBER_EMAIL_DUPLICATED)
    data class Unknown(val className: String): MemberError(ResponseEnums.ERROR)
}
