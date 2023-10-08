package me.golf.blog.product.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.blog.commonutils.IntegrationTest
import me.golf.blog.global.jwt.TokenProvider
import me.golf.blog.global.security.CustomUserDetailsService
import me.golf.blog.product.member.dto.MemberSavePostRequestDto
import me.golf.blog.product.member.dto.MemberSaveRequestDto
import me.golf.blog.product.member.dto.MemberUpdatePutRequestDto
import me.golf.blog.product.member.dto.MemberUpdateRequestDto
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberRepository
import me.golf.blog.product.member.util.GivenMember
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MemberIntegrationTest

@Autowired
constructor(
    private val memberRepository: MemberRepository,
    private val objectMapper: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
): IntegrationTest() {

    private var member: Member = GivenMember.toMember()
    private var token: String = ""

    private val simpleMemberResponse = responseFields(
        fieldWithPath("memberId").description("회원 식별자"),
        fieldWithPath("email").description("회원 이메일")
    )

    @BeforeEach
    fun setUp() {
        member = memberRepository.save(GivenMember.toMember())

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        token = tokenProvider.createToken(member.id, authToken).accessToken
    }

    @AfterEach
    fun destroy() {
        memberRepository.delete(member)
    }

    @Test
    fun `저장하는데 성공하는 인수 테스트`() {
        val requestDto = MemberSavePostRequestDto(
            email = "ktn@naver.com",
            password = GivenMember.PASSWORD,
            passwordConfirm = GivenMember.PASSWORD,
            name = GivenMember.NAME,
            nickname = "newNickname",
            description = GivenMember.DESCRIPTION,
            company = GivenMember.COMPANY,
            profileImageUrl = GivenMember.PROFILE_IMAGE,
            experience = GivenMember.EXPERIENCE,
            jobType = GivenMember.jobType.name
        )


        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("member/save/request",
                requestFields(
                    fieldWithPath("email").description("회원 이메일"),
                    fieldWithPath("password").description("회원 비밀번호"),
                    fieldWithPath("passwordConfirm").description("회원 비밀번호 확인"),
                    fieldWithPath("name").description("회원 이름"),
                    fieldWithPath("nickname").description("회원 별칭"),
                    fieldWithPath("description").description("자기 소개"),
                    fieldWithPath("company").description("재직 중 회사 명/재학 중 학교 명"),
                    fieldWithPath("profileImageUrl").description("프로필 이미지 signed url"),
                    fieldWithPath("experience").description("연차"),
                    fieldWithPath("jobType").description("재직 상태")
                )
            ))
            .andDo(document("member/save/response",
                simpleMemberResponse
            ))
    }

    @Test
    fun `회원 정보 저장 시 회원 비밀번호와 비밀번호 확인이 틀리면 예외`() {
        val requestDto = MemberSavePostRequestDto(
            email = "ktn@naver.com",
            password = GivenMember.PASSWORD,
            passwordConfirm = GivenMember.PASSWORD,
            name = GivenMember.NAME,
            nickname = "newNickname",
            description = GivenMember.DESCRIPTION,
            company = GivenMember.COMPANY,
            profileImageUrl = GivenMember.PROFILE_IMAGE,
            experience = GivenMember.EXPERIENCE,
            jobType = GivenMember.jobType.name
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
            .andExpect(status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `회원 정보 업데이트 성공하는 인수 테스트`() {
        val requestDto = MemberUpdatePutRequestDto(
            nickname = GivenMember.NICKNAME,
            name = GivenMember.NAME,
            description = GivenMember.DESCRIPTION,
            company = "newCompany",
            profileImageUrl = GivenMember.PROFILE_IMAGE,
            experience = 4,
            jobType = GivenMember.jobType.name
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(put("/members")
            .header("Authorization", "Bearer $token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("member/update/request",
                requestFields(
                    fieldWithPath("name").description("회원 이름"),
                    fieldWithPath("nickname").description("회원 별칭"),
                    fieldWithPath("description").description("자기 소개"),
                    fieldWithPath("company").description("재직 중 회사 명/재학 중 학교 명"),
                    fieldWithPath("profileImageUrl").description("프로필 이미지 signed url"),
                    fieldWithPath("experience").description("연차"),
                    fieldWithPath("jobType").description("재직 상태")
                )
            ))
            .andDo(document("member/update/response", simpleMemberResponse))
    }

    @Test
    fun `회원 정보를 단건으로 조회하는데 성공하는 인수 테스트`() {
        mockMvc.perform(get("/members/detail")
            .header("Authorization", "Bearer $token")
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("member/detail/response", responseFields(
                fieldWithPath("memberId").description("회원 식별자"),
                fieldWithPath("email").description("회원 이메일"),
                fieldWithPath("nickname").description("회원 별칭"),
                fieldWithPath("description").description("자기 소개"),
                fieldWithPath("company").description("재직 중 회사 명/재학 중 학교 명"),
                fieldWithPath("profileImageUrl").description("프로필 이미지 signed url"),
                fieldWithPath("experience").description("연차"),
                fieldWithPath("jobType").description("재직 상태"),
                fieldWithPath("followerCount").description("회원 팔로워 수"),
                fieldWithPath("followCount").description("회원 팔로잉 수"),
                fieldWithPath("boardCount").description("회원 보유 게시판 갯수")
            )))
    }

    @Test
    fun `회원 탈퇴 성공하는 인수 테스트`() {
        mockMvc.perform(delete("/members")
            .header("Authorization", "Bearer $token")
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("member/withdraw"))
    }
}