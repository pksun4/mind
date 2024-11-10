package com.mind.api.domain.board

import com.mind.core.domain.board.Board
import com.mind.core.enums.BoardEnums
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

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
    fun make(board: Board) = BoardResponse(
        id = board.id!!,
        type = board.type,
        title = board.title,
        createdAt = board.createdAt!!
    )
}
