package me.golf.blog.product.member.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.golf.blog.product.member.dto.MemberDetailResponseDto
import me.golf.blog.product.member.dto.MemberSaveRequestDto
import me.golf.blog.product.member.dto.MemberUpdateRequestDto
import me.golf.blog.product.member.handler.MemberRepositoryHandler
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.util.GivenMember
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class MemberServiceTest {

    private val memberRepositoryHandler = mockk<MemberRepositoryHandler>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private lateinit var memberService: MemberService
    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        member = GivenMember.toMember()
        memberService = MemberService(memberRepositoryHandler, passwordEncoder)
    }

    @Test
    fun `회원 정보 저장에 성공해야한다`() {
        // given
        val requestDto = MemberSaveRequestDto(
            email = GivenMember.EMAIL,
            password = GivenMember.PASSWORD,
            name = GivenMember.NAME,
            nickname = GivenMember.NICKNAME,
            description = GivenMember.DESCRIPTION,
            company = GivenMember.COMPANY,
            profileImageUrl = GivenMember.PROFILE_IMAGE,
            experience = GivenMember.EXPERIENCE,
            jobType = GivenMember.jobType
        )

        member.id = 1L

        every { passwordEncoder.encode(any()) } returns "#$#abs"
        every { memberRepositoryHandler.saveProcess(any()) } returns member

        // when
        val responseDto = memberService.save(requestDto)

        // then
        assertThat(member.id).isEqualTo(responseDto.memberId)
        verify { memberRepositoryHandler.saveProcess(any()) }
    }

    @Test
    fun `회원 정보 업데이트에 성공해야한다`() {
        // given
        val requestDto = MemberUpdateRequestDto(
            nickname = GivenMember.NICKNAME,
            name = GivenMember.NAME,
            description = GivenMember.DESCRIPTION,
            company = GivenMember.COMPANY,
            profileImageUrl = GivenMember.PROFILE_IMAGE,
            experience = GivenMember.EXPERIENCE,
            jobType = GivenMember.jobType,
            memberId = 1L
        )

        member.id = 1L

        every { memberRepositoryHandler.beforeUpdateProcess(any(), any()) } returns member

        // when
        val responseDto = memberService.update(requestDto)

        // then
        assertThat(responseDto.memberId).isEqualTo(member.id)
        verify { memberRepositoryHandler.beforeUpdateProcess(any(), any()) }
    }

    @Test
    fun `회원 탈퇴에 성공한다`() {
        // given
        val memberId = 1L

        every { memberRepositoryHandler.beforeWithdrawProcess(any()) } returns member

        // when
        memberService.withdraw(memberId)

        // then
        verify { memberRepositoryHandler.beforeWithdrawProcess(any()) }
    }

    @Test
    fun `회원 상세 정보를 조회하는데 성공한다`() {
        // given
        val memberId = 1L

        val responseDto = MemberDetailResponseDto(
            memberId = memberId,
            email = GivenMember.EMAIL,
            nickname = GivenMember.NICKNAME,
            description = GivenMember.DESCRIPTION,
            company = GivenMember.COMPANY,
            profileImageUrl = GivenMember.PROFILE_IMAGE,
            experience = GivenMember.EXPERIENCE,
            jobType = GivenMember.jobType,
            followerCount = 0,
            followCount = 0,
            boardCount = 0
        )

        every { memberRepositoryHandler.getOneProcess(any()) } returns responseDto

        // when
        val result = memberService.getDetail(memberId)

        // then
        assertThat(result.email).isEqualTo(responseDto.email)
        verify { memberRepositoryHandler.getOneProcess(any()) }
    }
}