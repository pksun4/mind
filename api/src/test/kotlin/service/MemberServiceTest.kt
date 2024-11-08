package service

import com.mind.api.domain.member.MemberError
import com.mind.api.domain.member.MemberService
import com.mind.core.domain.member.Member
import com.mind.core.domain.member.MemberRepository
import com.mind.core.domain.member.MemberRequest
import com.mind.core.enums.GenderEnums
import com.mind.core.enums.ResponseEnums
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MemberServiceTest {

    private val memberRepository: MemberRepository = mockk()
    private val memberService: MemberService = MemberService(memberRepository)

    companion object {
        private const val EMAIL = "mind@gmail.com"
        private const val PASSWORD = "1234"
        private const val NAME = "마인드"
        private val GENDER = GenderEnums.M
    }

    @Test
    fun `success signUp`() {
        val memberParams = settingMemberParams()
        val member = settingMember()

        coEvery { memberRepository.findByEmail(any()) } answers { null }
        coEvery { memberRepository.save(any()) } answers { member }
        runBlocking {
            memberService.signUp(memberParams).fold(
                { fail -> Assertions.assertEquals(fail.responseEnums, ResponseEnums.ERROR) },
                { success ->
                    assertEquals(success.email, memberParams.email)
                    assertEquals(success.name, memberParams.name)
                    assertEquals(success.gender, memberParams.gender)
                }
            )
            coVerify(exactly = 1) { memberRepository.findByEmail(any()) }
            coVerify(exactly = 1) { memberRepository.save(any()) }
        }
    }

    @Test
    fun `fail signup`() {
        val memberParams = settingMemberParams()

        coEvery { memberRepository.findByEmail(any()) } answers { settingMember() }
        runBlocking {
            memberService.signUp(memberParams).fold(
                { fail -> Assertions.assertEquals(fail, MemberError.MemberEmailDuplicated) },
                { }
            )
            coVerify(exactly = 1) { memberRepository.findByEmail(any()) }
        }
    }

    private fun settingMember() = Member(
        id = 1,
        email = EMAIL,
        password = PASSWORD,
        name = "마인드",
        gender = GENDER
    )

    private fun settingMemberParams() = MemberRequest(
        email = EMAIL,
        password = PASSWORD,
        name = NAME,
        gender = GENDER
    )
}
