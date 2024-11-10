package service

import com.mind.api.domain.board.BoardError
import com.mind.api.domain.board.BoardRequest
import com.mind.api.domain.board.BoardSaveRequest
import com.mind.api.domain.board.BoardService
import com.mind.core.domain.board.Board
import com.mind.core.domain.board.BoardRepository
import com.mind.core.domain.board.BoardRepositorySupport
import com.mind.core.enums.BoardEnums
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BoardServiceTest {

    private val boardRepository: BoardRepository = mockk()
    private val boardRepositorySupport: BoardRepositorySupport = mockk()
    private val boardService: BoardService = BoardService(boardRepository, boardRepositorySupport)

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

    @Test
    fun `success get board`() {
        val board = settingBoard()

        coEvery { boardRepositorySupport.findActiveBoardById(any()) } answers { board }
        runBlocking {
            boardService.getBoard(ID).fold(
                { },
                { success -> assertEquals(success.id, ID) }
            )
            coVerify(exactly = 1) { boardRepositorySupport.findActiveBoardById(ID) }
        }
    }

    @Test
    fun `fail get board`() {
        coEvery { boardRepositorySupport.findActiveBoardById(any()) } answers { null }
        runBlocking {
            boardService.getBoard(ID).fold(
                { fail -> assertEquals(fail, BoardError.BoardNone) },
                { }
            )
        }
    }

    @Test
    fun `success get board list`() {
        val boardRequest = BoardRequest(
            type = TYPE,
            title= null
        )

        coEvery { boardRepositorySupport.countActiveBoardList(any(), any()) } answers { 1 }
        coEvery { boardRepositorySupport.findActiveBoardList(any(), any(), any()) } answers { arrayOf(settingBoard()).toMutableList() }
        runBlocking {
            boardService.getBoardList(boardRequest).fold(
                { },
                { success -> assertEquals(success.list?.size, 1) }
            )
        }
    }

    private fun settingBoard() = Board(
        id = ID,
        type = TYPE,
        title = TITLE,
        content = CONTENT
    )
}
