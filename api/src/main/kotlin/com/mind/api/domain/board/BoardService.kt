package com.mind.api.domain.board

import arrow.core.left
import arrow.core.right
import com.mind.api.domain.member.MemberError
import com.mind.core.domain.board.BoardRepository
import com.mind.core.enums.ResponseEnums
import com.mind.core.util.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    val boardRepository: BoardRepository
) {

    @Transactional
    fun saveBoard(boardSaveRequest: BoardSaveRequest) =
        runCatching {
            boardRepository.save(BoardSaveRequest.convert(boardSaveRequest)).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
    private fun Throwable.throwUnknownError() = MemberError.Unknown(this.javaClass.name).left()
}

sealed class BoardError(
    val responseEnums: ResponseEnums
) {
    data class Unknown(val className: String): BoardError(ResponseEnums.ERROR)
}
