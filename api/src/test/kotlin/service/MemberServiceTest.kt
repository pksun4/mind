package service

import com.mind.api.domain.member.MemberError
import com.mind.api.domain.member.MemberRequest
import com.mind.api.domain.member.MemberService
import com.mind.api.domain.member.MemberUpdateRequest
import com.mind.api.security.CustomUser
import com.mind.core.domain.member.Member
import com.mind.core.domain.member.MemberRepository
import com.mind.core.enums.GenderEnums
import com.mind.core.enums.ResponseEnums
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails


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

        coEvery { memberRepository.findMemberByEmail(any()) } answers { null }
        coEvery { memberRepository.save(any()) } answers { member }
        runBlocking {
            memberService.signUp(memberParams).fold(
                { },
                { success ->
                    assertEquals(success.email, memberParams.email)
                    assertEquals(success.name, memberParams.name)
                    assertEquals(success.gender, memberParams.gender)
                }
            )
            coVerify(exactly = 1) { memberRepository.findMemberByEmail(any()) }
            coVerify(exactly = 1) { memberRepository.save(any()) }
        }
    }

    @Test
    fun `fail signup`() {
        val memberParams = settingMemberParams()

        coEvery { memberRepository.findMemberByEmail(any()) } answers { settingMember() }
        runBlocking {
            memberService.signUp(memberParams).fold(
                { fail -> Assertions.assertEquals(fail, MemberError.MemberEmailDuplicated) },
                { }
            )
            coVerify(exactly = 1) { memberRepository.findMemberByEmail(any()) }
        }
    }

    @Test
    fun `success update member`() {
        settingSecurity()
        val memberUpdateParams = MemberUpdateRequest(currentPassword = "1234", changePassword = "9999")

        coEvery { memberRepository.findMemberById(any()) } answers { settingMember() }
        runBlocking {
            memberService.updateMember(memberUpdateParams).fold(
                { },
                { success -> assertEquals(success, Unit) }
            )
            coVerify(exactly = 1) { memberRepository.findMemberById(any()) }
        }
    }

    @Test
    fun `fail update member`() {
        settingSecurity()
        val memberUpdateParams = MemberUpdateRequest(currentPassword = "1234", changePassword = "9999")

        coEvery { memberRepository.findMemberById(any()) } answers { null }
        runBlocking {
            memberService.updateMember(memberUpdateParams).fold(
                { fail -> assertEquals(fail.responseEnums, ResponseEnums.MEMBER_NONE) },
                { }
            )
            coVerify { memberRepository.findMemberById(any()) }
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

    private fun settingSecurity() {
        val user: UserDetails = CustomUser(
            1L,
            EMAIL,
            PASSWORD,
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )
        val context = SecurityContextHolder.getContext()
        context.authentication = UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
    }

}
