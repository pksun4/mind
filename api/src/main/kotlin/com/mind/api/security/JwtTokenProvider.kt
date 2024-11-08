package com.mind.api.security

import com.mind.core.util.logger
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class JwtTokenProvider {
    companion object {
        const val TOKEN_EXPIRATION_MS: Long = 1000 * 60 * 60 * 24
        const val REFRESH_EXPIRATION_MS: Long = 1000 * 60 * 60 * 24 * 1
    }

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    fun createToken(authentication: Authentication): String {
        val authorities: String = authentication
            .authorities
            .joinToString(",", transform = GrantedAuthority::getAuthority)

        val now = Date()
        val accessExpiration = Date(now.time + TOKEN_EXPIRATION_MS)
        return Jwts
            .builder()
            .setSubject(authentication.name)
            .claim("auth", authorities)
            .claim("memberKey", (authentication.principal as CustomUser).memberKey)
            .setIssuedAt(now)
            .setExpiration(accessExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createRefreshToken(authentication: Authentication): String {
        return Jwts
            .builder()
            .setExpiration(Date(Date().time + REFRESH_EXPIRATION_MS))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun parseToken(token: String) : Authentication {
        val claims: Claims = getClaims(token)

        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val memberKey = claims["memberKey"] ?: throw RuntimeException("잘못된 토큰입니다.")

        // authority
        val authorities: Collection<GrantedAuthority> = (auth as String)
            .split(",")
            .map { SimpleGrantedAuthority(it) }

        val principal: UserDetails = CustomUser(memberKey.toString().toLong(), claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateToken(token: String): Boolean {
        runCatching {
            getClaims(token)
            return true
        }.getOrElse {
            when(it) {
                is SecurityException -> {}
                is MalformedJwtException -> {}
                is ExpiredJwtException -> {}
                is UnsupportedJwtException -> {}
                is IllegalArgumentException -> {}
                else -> {}
            }
            logger().error(it.message)
        }
        return false
    }

    private fun getClaims(token: String) = Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .body
}
