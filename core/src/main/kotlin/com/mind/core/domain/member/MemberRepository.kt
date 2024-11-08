package com.mind.core.domain.member

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MemberRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Member::class.java) {

}

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String) : Member?
}
