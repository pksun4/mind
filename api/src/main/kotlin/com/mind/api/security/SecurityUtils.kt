package com.mind.api.security

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object SecurityUtils {
    fun getCurrentUser() : CustomUserToken? {
            SecurityContextHolder.getContext().authentication?.let {
                if (it is AnonymousAuthenticationToken) {
                    return null
                }

                val principal = it.principal as CustomUser
                return CustomUserToken(
                    principal.memberKey,
                    principal.authorities
                )
            } ?: return null
        }

    fun matchPassword(checkPassword: String, savedPassword:String) =
        cryptPasswordEncoder().matches(checkPassword, savedPassword)
    fun String.passwordEncode(): String = cryptPasswordEncoder().encode(this)

    private fun cryptPasswordEncoder() = BCryptPasswordEncoder()
}
