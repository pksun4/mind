package com.mind.api.domain.board

import com.mind.api.common.dto.PagingRequestData
import com.mind.core.domain.board.Board
import com.mind.core.enums.BoardEnums
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import kotlin.math.max
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class BoardSaveRequest(
    @Schema(name = "게시판 유형")
    val type: BoardEnums,
    @Schema(name = "제목")
    val title: String,
    @Schema(name = "내용")
    val content: String?
) {
    companion object {
        fun convert(boardSaveRequest: BoardSaveRequest) = Board(
            type = boardSaveRequest.type,
            title = boardSaveRequest.title,
            content = boardSaveRequest.content
        )
    }
}

class BoardResponse(
    @Schema(name = "아이디")
    val id: Long,
    @Schema(name = "게시판 유형")
    val type: BoardEnums,
    @Schema(name = "제목")
    val title: String,
    @Schema(name = "등록일")
    val createdAt: LocalDateTime
) {
    companion object {
        fun make(board: Board) = BoardResponse(
            id = board.id!!,
            type = board.type,
            title = board.title,
            createdAt = board.createdAt!!
        )

        fun make(boardList: MutableList<Board>) = boardList.map {
            BoardResponse(
                id = it.id!!,
                type = it.type,
                title = it.title,
                createdAt = it.createdAt!!
            )
        }
    }
}

class BoardRequest(
    @Schema(name = "게시판 유형")
    val type: BoardEnums,
    @Schema(name = "제목")
    val title: String?
) : PagingRequestData() {
    companion object {
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_SIZE = 10
        private const val DEFAULT_SORT_BY = "id"
        fun convertPaging(page: Int, size: Int): PageRequest {
            return PageRequest.of(
                max(page, DEFAULT_PAGE).minus(1),
                max(size, DEFAULT_SIZE),
                Sort.Direction.ASC,
                DEFAULT_SORT_BY
            )
        }
    }
}
