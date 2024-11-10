package com.mind.core.domain.board

import com.mind.core.domain.board.QBoard.board
import com.mind.core.enums.BoardEnums
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Board::class.java) {
    fun findActiveBoardById(id: Long): Board? = jpaQueryFactory
        .selectFrom(board)
        .where(
            board.id.eq(id),
            board.isDeleted.isFalse
        )
        .fetchOne()

    fun findActiveBoardList(type: BoardEnums, title: String?, paging: Pageable): MutableList<Board>? = jpaQueryFactory
        .selectFrom(board)
        .where(
            board.isDeleted.isFalse,
            board.type.eq(type),
            eqTitle(title)
        )
        .orderBy(board.id.desc())
        .offset(paging.offset)
        .limit(paging.pageSize.toLong())
        .fetch()

    fun countActiveBoardList(type: BoardEnums, title: String?) : Int = jpaQueryFactory
        .selectFrom(board)
        .where(
            board.isDeleted.isFalse,
            board.type.eq(type),
            eqTitle(title)
        ).fetch().count()

    fun eqTitle(title: String?): BooleanExpression? {
        return if (StringUtils.isNullOrEmpty(title)) null else board.title.containsIgnoreCase(title)
    }
}

@Repository
interface BoardRepository : JpaRepository<Board, Long> {
    fun findBoardById(id: Long): Board?
}
