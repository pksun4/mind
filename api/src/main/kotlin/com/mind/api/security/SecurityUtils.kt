package com.mind.api.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object SecurityUtils {

    private fun cryptPasswordEncoder() = BCryptPasswordEncoder()

    fun String.passwordEncode(): String = cryptPasswordEncoder().encode(this)

}
