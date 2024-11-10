package service

import com.mind.api.domain.board.BoardSaveRequest
import com.mind.api.domain.board.BoardService
import com.mind.core.domain.board.Board
import com.mind.core.domain.board.BoardRepository
import com.mind.core.enums.BoardEnums
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BoardServiceTest {

    private val boardRepository: BoardRepository = mockk()
    private val boardService: BoardService = BoardService(boardRepository)

    companion object {
        private const val ID = 1L
        private const val TITLE = "제목"
        private const val CONTENT = "내용"
        private val TYPE = BoardEnums.SHARE
    }

    @Test
    fun `success save board`() {
        val boardSaveParams = BoardSaveRequest(
            type = TYPE,
            title = TITLE,
            content = CONTENT
        )
        val board = settingBoard()

        coEvery { boardRepository.save(any()) } answers { board }
        runBlocking {
            boardService.saveBoard(boardSaveParams).fold(
                { },
                { success ->
                    assertEquals(success.type, boardSaveParams.type)
                    assertEquals(success.title, boardSaveParams.title)
                    assertEquals(success.content, boardSaveParams.content)
                }
            )
            coVerify(exactly = 1) { boardRepository.save(any()) }
        }
    }

    private fun settingBoard() = Board(
        id = ID,
        type = TYPE,
        title = TITLE,
        content = CONTENT
    )
}
