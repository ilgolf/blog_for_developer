package me.golf.blog.product.member.handler

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.golf.blog.product.member.dto.MemberDetailResponseDto
import me.golf.blog.product.member.dto.MemberUpdateHandlerRequestDto
import me.golf.blog.product.member.exception.MemberException
import me.golf.blog.product.member.persist.JobType
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberCustomRepository
import me.golf.blog.product.member.persist.repository.MemberRepository
import me.golf.blog.product.member.util.GivenMember
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.data.repository.findByIdOrNull

class MemberRepositoryHandlerTest {

    private val memberRepository = mockk<MemberRepository>()
    private val memberCustomRepository = mockk<MemberCustomRepository>()
    private lateinit var memberRepositoryHandler: MemberRepositoryHandler

    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        memberRepositoryHandler = MemberRepositoryHandler(memberRepository, memberCustomRepository)
        member = GivenMember.toMember()
    }

    @Test
    fun `회원 정보 저장하기 전 유효성 검증 후 데이터를 저장한다`() {
        // given
        member.id = 3L

        every { memberRepository.save(any()) } returns member
        every { memberRepository.existsByEmail(any()) } returns false
        every { memberRepository.existsByNickname(any()) } returns false

        // when
        memberRepositoryHandler.save(member)

        // then
        verify { memberRepository.existsByEmail(any()) }
        verify { memberRepository.existsByNickname(any()) }
    }
    @Test
    fun `저장 전 유효성 검증에 실패하면 이미 존재하는 회원입니다 예외 발생`() {
        // given
        member.id = 3L

        every { memberRepository.save(any()) } returns member
        every { memberRepository.existsByEmail(any()) } returns true
        every { memberRepository.existsByNickname(any()) } returns false

        // when
        val exception = catchException { memberRepositoryHandler.save(member) }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(MemberException.AlreadyExistException::class.java) },
            { assertThat(exception.message).isEqualTo("이미 존재하는 회원입니다.: ${member.email}") }
        )
    }

    @Test
    fun `회원 정보 업데이트 전 유효성 검증 후 데이터를 업데이트한다`() {
        // given
        member.id = 3L

        val requestDto = MemberUpdateHandlerRequestDto(
            nickname = "newNickname",
            name = "노경태",
            description = "안녕하세요 2년차 서버 엔지니어입니다.",
            jobType = JobType.WORKING,
            company = "Sir.Loin",
            profileImageUrl = "mockPath",
            experience = 3,
            memberId = 3L,
        )

        every { memberRepository.existsByIdNotAndNickname(any(), any()) } returns false
        every { memberRepository.findByIdOrNull(any()) } returns member

        // when
        memberRepositoryHandler.update(requestDto)

        // then
        verify { memberRepository.existsByIdNotAndNickname(any(), any()) }
        verify { memberRepository.findByIdOrNull(any()) }
    }

    @Test
    fun `회원 정보 업데이트 전 회원이 존재하지 않으면 존재하지 않는 회원입니다 예외 발생`() {
        // given
        member.id = 3L

        val requestDto = MemberUpdateHandlerRequestDto(
            nickname = "newNickname",
            name = "노경태",
            description = "안녕하세요 2년차 서버 엔지니어입니다.",
            jobType = JobType.WORKING,
            company = "Sir.Loin",
            profileImageUrl = "mockPath",
            experience = 3,
            memberId = 3L,
        )

        every { memberRepository.existsByIdNotAndNickname(any(), any()) } returns false
        every { memberRepository.findByIdOrNull(any()) } returns null

        // when
        val exception = catchException { memberRepositoryHandler.update(requestDto) }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(MemberException.MemberNotFoundException::class.java) },
            { assertThat(exception.message).isEqualTo("회원을 찾지 못하였습니다.: ${member.id}") }
        )
    }

    @Test
    fun `회원 업데이트 전 유효성 검증에 실패 시 이미 존재하는 회원입니다 예외 발생`() {
        // given
        member.id = 3L
        val newNickname = "newNickname"

        val requestDto = MemberUpdateHandlerRequestDto(
            nickname = "newNickname",
            name = "노경태",
            description = "안녕하세요 2년차 서버 엔지니어입니다.",
            jobType = JobType.WORKING,
            company = "Sir.Loin",
            profileImageUrl = "mockPath",
            experience = 3,
            memberId = 3L,
        )

        every { memberRepository.existsByIdNotAndNickname(any(), any()) } returns true
        every { memberRepository.findByIdOrNull(any()) } returns member

        // when
        val exception = catchException { memberRepositoryHandler.update(requestDto) }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(MemberException.AlreadyExistException::class.java) },
            { assertThat(exception.message).isEqualTo("이미 존재하는 회원입니다.: $newNickname") }
        )
    }

    @Test
    fun `회원 탈퇴 전 회원 유무에 대한 유효성을 검증한다`() {
        // given
        member.id = 3L

        every { memberRepository.findByIdOrNull(any()) } returns member

        // when
        memberRepositoryHandler.withDraw(member.id)

        // then
        verify { memberRepository.findByIdOrNull(any()) }
    }

    @Test
    fun `회원 탈퇴 전 회원 유무에 대한 유효성을 검증 실패 시 회원을 찾지 못하였습니다 예외 발생`() {
        // given
        member.id = 3L

        every { memberRepository.findByIdOrNull(any()) } returns null

        // when
        val exception = catchException { memberRepositoryHandler.withDraw(member.id) }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(MemberException.MemberNotFoundException::class.java) },
            { assertThat(exception.message).isEqualTo("회원을 찾지 못하였습니다.: ${member.id}") }
        )
    }

    @Test
    fun `회원 정보를 가져 오는데 성공한다`() {
        // given
        val responseDto = MemberDetailResponseDto(
            memberId = 3L,
            email = "ilgolc@naver.com",
            nickname = "nokt",
            description = GivenMember.DESCRIPTION,
            company = GivenMember.COMPANY,
            profileImageUrl = GivenMember.PROFILE_IMAGE,
            experience = GivenMember.EXPERIENCE,
            jobType = GivenMember.jobType,
            followerCount = 0,
            followCount = 0,
            boardCount = 0
        )

        every { memberCustomRepository.findDetailById(any()) } returns responseDto

        // when
        memberRepositoryHandler.getDetail(member.id)

        // then
        verify { memberCustomRepository.findDetailById(any()) }
    }

    @Test
    fun `회원 정보를 못가져오면 회원을 찾지 못하였습니다 예외 발생`() {
        // given
        every { memberCustomRepository.findDetailById(any()) } returns null

        // when
        val exception = catchException { memberRepositoryHandler.getDetail(member.id) }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(MemberException.MemberNotFoundException::class.java) },
            { assertThat(exception.message).isEqualTo("회원을 찾지 못하였습니다.: ${member.id}") }
        )
    }
}