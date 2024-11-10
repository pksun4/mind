package com.mind.api.domain.member

import arrow.core.left
import arrow.core.right
import com.mind.api.security.SecurityUtils
import com.mind.api.security.SecurityUtils.passwordEncode
import com.mind.core.domain.member.MemberRepository
import com.mind.core.domain.member.MemberRole
import com.mind.core.enums.ResponseEnums
import com.mind.core.util.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun signUp(memberRequest: MemberRequest) =
        runCatching {
            memberRepository.findMemberByEmail(memberRequest.email)?.let {
                MemberError.MemberEmailDuplicated.left()
            } ?: memberRepository.save(
                MemberRequest.convert(memberRequest).apply {
                    this.memberRole = mutableListOf(
                        MemberRole.grantMember(this)
                    )
                }
            ).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    @Transactional
    fun updateMember(memberUpdateRequest: MemberUpdateRequest) =
        runCatching {
            SecurityUtils.getCurrentUser()?.let {
                memberRepository.findMemberById(it.memberKey)?.let { member ->
                    if (SecurityUtils.matchPassword(memberUpdateRequest.currentPassword, member.password)) {
                        member.password = memberUpdateRequest.changePassword.passwordEncode()
                        Unit.right();
                    } else {
                        MemberError.MemberPasswordIncorrect.left()
                    }
                } ?: MemberError.MemberNone.left()
            } ?: MemberError.MemberNoneToken.left()
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
    data object MemberEmailDuplicated : MemberError(ResponseEnums.MEMBER_EMAIL_DUPLICATED)
    data object MemberNone : MemberError(ResponseEnums.MEMBER_NONE)
    data object MemberNoneToken : MemberError(ResponseEnums.MEMBER_NONE)
    data object MemberPasswordIncorrect : MemberError(ResponseEnums.MEMBER_PASSWORD_INCORRECT)
    class Unknown(val className: String): MemberError(ResponseEnums.ERROR)
}
