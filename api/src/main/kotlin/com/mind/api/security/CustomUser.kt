package com.mind.api.security

import java.io.Serial
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val memberKey: Long,
    userEmail: String,
    userPassword: String,
    authorities: Collection<GrantedAuthority>
) : User(userEmail, userPassword, authorities) {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 4583945307331286192L
    }
}
