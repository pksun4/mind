package com.mind.api.domain.board

import com.mind.api.common.dto.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/boards")
@Tag(name = "게시판 관리")
class BoardController(
    private val boardService: BoardService
) {

    @PostMapping
    @Operation(summary = "게시판 등록")
    fun saveBoard(boardSaveRequest: BoardSaveRequest) = boardService.saveBoard(boardSaveRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(BoardResponse.make(it)) }
    )

    @GetMapping("/{id}")
    @Operation(summary = "게시글 조회")
    fun getBoard(@PathVariable id: Long) = boardService.getBoard(id).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(BoardResponse.make(it)) }
    )

    @GetMapping
    @Operation(summary = "게시판 목록 조회")
    fun getBoardList(boardRequest: BoardRequest) = boardService.getBoardList(boardRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(it) }
    )

}