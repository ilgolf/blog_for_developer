package me.golf.blog.product.member.persist

import me.golf.blog.product.member.dto.MemberUpdateHandlerRequestDto
import me.golf.blog.product.member.dto.MemberUpdateRequestDto
import me.golf.blog.product.member.util.GivenMember
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class MemberTest {

    private lateinit var member: Member

    @BeforeEach
    fun setup() {
        member = GivenMember.toMember()
    }

    @Test
    fun `회원 생성 시 초기 metrics 정보는 모두 0이다`() {
        // given
        val newMember = Member(
            id = 1L,
            email = "golf@google.co.kr",
            password = "12345678",
            name = "김성희",
            nickname = "부엉희",
            description = "3년차 QC입니다.",
            jobType = JobType.WORKING,
            company = "CJ 제일제당",
            experience = 10,
            profileImageUrl = "https://s3.amazon.image/s3"
        )

        // when
        val memberMetrics = newMember.memberMetrics

        // then
        assertAll(
            { assertThat(memberMetrics.boardCount).isEqualTo(0) },
            { assertThat(memberMetrics.followingCount).isEqualTo(0) },
            { assertThat(memberMetrics.followCount).isEqualTo(0) }
        )
    }

    @Test
    fun `회원 정보를 수정한다`() {
        // given
        val requestDto = MemberUpdateHandlerRequestDto(
            nickname = "야옹이",
            name = "김경태",
            description = "안녕 나 신입 개발자야",
            jobType = JobType.WORKING,
            company = "내 회사",
            profileImageUrl = "https://www.s3.amazone/image/3",
            experience = 3,
            memberId = 1L
        )

        // when
        member.update(requestDto)

        // then
        assertAll(
            { assertThat(member.nickname).isEqualTo("야옹이") },
            { assertThat(member.name).isEqualTo("김경태") },
            { assertThat(member.description).isEqualTo("안녕 나 신입 개발자야") },
            { assertThat(member.jobType).isEqualTo(JobType.WORKING) },
            { assertThat(member.company).isEqualTo("내 회사") },
            { assertThat(member.profileImageUrl).isEqualTo("https://www.s3.amazone/image/3") },
            { assertThat(member.experience).isEqualTo(3) },
        )
    }

    @Test
    fun `회원이 탈퇴한다`() {
        // given

        // when
        member.withdraw()

        // then
        assertThat(member.activated).isFalse()
    }

    @Test
    fun `회원의 게시판 개수가 올라간다`() {
        // given

        // when
        member.memberMetrics.plusBoardCount()

        // then
        assertThat(member.memberMetrics.boardCount).isEqualTo(1)
    }
}