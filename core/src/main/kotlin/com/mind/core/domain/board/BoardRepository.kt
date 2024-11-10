package com.mind.core.domain.board

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Board::class.java) {

}

@Repository
interface BoardRepository : JpaRepository<Board, Long> {

}
