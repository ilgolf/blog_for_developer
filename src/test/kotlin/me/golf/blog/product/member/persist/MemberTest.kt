package me.golf.blog.product.member.persist

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
    fun `회원 정보를 수정한다`() {
        // given
        val requestDto = MemberUpdateRequestDto(
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