package com.mind.api.security

import com.mind.core.domain.member.Member
import com.mind.core.domain.member.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        memberRepository.findByEmail(username)?.let {
            createUserDetails(it)
        } ?: throw UsernameNotFoundException("해당 유저가 없습니다.")

    private fun createUserDetails(member: Member) : UserDetails =
        CustomUser(
            member.id!!,
            member.email,
            member.password,
            member.memberRole!!.map { SimpleGrantedAuthority("ROLE_${it.role}") }
        )
}
