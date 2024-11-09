package com.mind.api.security

import org.springframework.security.core.GrantedAuthority

class CustomUserToken(
    val memberKey: Long,
    val role: Collection<GrantedAuthority>
)
