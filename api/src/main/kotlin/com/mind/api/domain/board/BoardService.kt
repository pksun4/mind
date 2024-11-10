package com.mind.api.domain.board

import arrow.core.left
import arrow.core.right
import com.mind.api.common.dto.PagingResponseData
import com.mind.api.common.dto.PagingWrapper
import com.mind.core.domain.board.BoardRepository
import com.mind.core.domain.board.BoardRepositorySupport
import com.mind.core.enums.ResponseEnums
import com.mind.core.util.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    val boardRepository: BoardRepository,
    val boardRepositorySupport: BoardRepositorySupport
) {

    @Transactional
    fun saveBoard(boardSaveRequest: BoardSaveRequest) =
        runCatching {
            boardRepository.save(BoardSaveRequest.convert(boardSaveRequest)).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    @Transactional
    fun getBoard(id: Long) =
        runCatching {
            boardRepositorySupport.findActiveBoardById(id)?.let {
                it.read()
                it.right()
            } ?: BoardError.BoardNone.left()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    @Transactional(readOnly = true)
    fun getBoardList(boardRequest: BoardRequest) =
        runCatching {
            val count = boardRepositorySupport.countActiveBoardList(boardRequest.type, boardRequest.title)
            if (count > 0) {
                val list = boardRepositorySupport.findActiveBoardList(
                    boardRequest.type,
                    boardRequest.title,
                    BoardRequest.convertPaging(boardRequest.page, boardRequest.size)
                )
                PagingWrapper(
                    paging = PagingResponseData(
                        total = count,
                        page = boardRequest.page,
                        size = boardRequest.size
                    ),
                    list = list!!.map { board -> BoardResponse.make(board) }
                ).right()
            } else {
                PagingWrapper(
                    paging = PagingResponseData(
                        total = 0,
                        page = boardRequest.page,
                        size = boardRequest.size
                    ),
                    list = null
                ).right()
            }
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.name}]", this)
    private fun Throwable.throwUnknownError() = BoardError.Unknown(this.javaClass.name).left()
}

sealed class BoardError(
    val responseEnums: ResponseEnums
) {
    data object BoardNone : BoardError(ResponseEnums.BOARD_NONE)
    data class Unknown(val className: String): BoardError(ResponseEnums.ERROR)
}
