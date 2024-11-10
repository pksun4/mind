package service

import com.mind.api.domain.board.BoardError
import com.mind.api.domain.board.BoardRequest
import com.mind.api.domain.board.BoardSaveRequest
import com.mind.api.domain.board.BoardService
import com.mind.api.domain.board.BoardUpdateRequest
import com.mind.api.security.CustomUser
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

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
            coVerify(exactly = 1) { boardRepositorySupport.findActiveBoardById(any()) }
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
            coVerify(exactly = 1) { boardRepositorySupport.countActiveBoardList(any(), any()) }
            coVerify(exactly = 1) { boardRepositorySupport.findActiveBoardList(any(), any(), any()) }
        }
    }

    @Test
    fun `success remove board`() {
        settingSecurity()
        val board = settingBoard()

        coEvery { boardRepositorySupport.findActiveBoardById(any()) } answers { board }
        runBlocking {
            boardService.removeBoard(ID).fold(
                { },
                { success -> assertEquals(success, Unit) }
            )
            coVerify(exactly = 1) { boardRepositorySupport.findActiveBoardById(any()) }
        }
    }

    @Test
    fun `fail remove board`() {
        coEvery { boardRepositorySupport.findActiveBoardById(any()) } answers { null }
        runBlocking {
            boardService.removeBoard(ID).fold(
                { fail -> assertEquals(fail, BoardError.BoardNone) },
                { }
            )
            coVerify(exactly = 1) { boardRepositorySupport.findActiveBoardById(any()) }
        }
    }

    @Test
    fun `success update board`() {
        settingSecurity()
        val boardUpdateParams = BoardUpdateRequest(
            title = TITLE,
            content = CONTENT
        )
        val board = settingBoard()

        coEvery { boardRepositorySupport.findActiveBoardById(any()) } answers { board }
        runBlocking {
            boardService.updateBoard(ID, boardUpdateParams).fold(
                { },
                { success ->
                    assertEquals(success.title, boardUpdateParams.title)
                    assertEquals(success.content, boardUpdateParams.content)
                }
            )
            coVerify(exactly = 1) { boardRepositorySupport.findActiveBoardById(any()) }
        }
    }

    @Test
    fun `fail update board`() {
        val boardUpdateParams = BoardUpdateRequest(
            title = TITLE,
            content = CONTENT
        )

        coEvery { boardRepositorySupport.findActiveBoardById(any()) } answers { null }
        runBlocking {
            boardService.updateBoard(ID, boardUpdateParams).fold(
                { fail -> assertEquals(fail, BoardError.BoardNone) },
                { }
            )
            coVerify(exactly = 1) { boardRepositorySupport.findActiveBoardById(any()) }
        }
    }

    private fun settingBoard() = Board(
        id = ID,
        type = TYPE,
        title = TITLE,
        content = CONTENT
    )

    private fun settingSecurity() {
        val user: UserDetails = CustomUser(
            memberKey = 1L,
            userEmail = "email",
            userPassword = "password",
            authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        )
        val context = SecurityContextHolder.getContext()
        context.authentication = UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
    }
}
