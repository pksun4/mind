package com.mind.api.domain.authentication

import arrow.core.left
import arrow.core.right
import com.mind.api.security.JwtTokenProvider
import com.mind.api.security.TokenRequest
import com.mind.api.security.TokenResponse
import com.mind.core.domain.member.MemberRepository
import com.mind.core.enums.ResponseEnums
import com.mind.core.util.logger
import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository
) {

    companion object {
        private const val GRANT_TYPE = "Bearer"
    }

    @Transactional
    suspend fun login(tokenRequest: TokenRequest) =
        runCatching {
            val authenticationToken = UsernamePasswordAuthenticationToken(
                tokenRequest.email,
                tokenRequest.password
            )

            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

            memberRepository.findByEmail(tokenRequest.email)?.let {
                buildToken(authentication).right()
            } ?: AuthenticationError.AuthenticationIncorrect.left()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            AuthenticationError.AuthenticationFail.left()
        }

    private fun buildToken(authentication: Authentication) = TokenResponse(
            grantType = GRANT_TYPE,
            accessToken = jwtTokenProvider.createToken(authentication),
            refreshToken = jwtTokenProvider.createRefreshToken(authentication)
        )

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
    private fun Throwable.throwUnknownError() = AuthenticationError.Unknown(this.javaClass.name).left()
}

sealed class AuthenticationError(
    val responseEnums: ResponseEnums
) {
    data object AuthenticationFail: AuthenticationError(ResponseEnums.AUTHENTICATION_FAIL)
    data object AuthenticationIncorrect: AuthenticationError(ResponseEnums.AUTHENTICATION_INCORRECT)
    data class Unknown(val className: String): AuthenticationError(ResponseEnums.ERROR)
}
