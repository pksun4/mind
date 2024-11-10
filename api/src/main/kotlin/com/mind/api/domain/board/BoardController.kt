package com.mind.api.domain.board

import com.mind.api.common.dto.ResponseData
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/boards")
class BoardController(
    private val boardService: BoardService
) {

    @PostMapping
    @Operation(summary = "게시판 등록")
    fun saveBoard(boardSaveRequest: BoardSaveRequest) = boardService.saveBoard(boardSaveRequest).fold(
        { ResponseData.fail(it.responseEnums) },
        { ResponseData.success(it) }
    )

}
