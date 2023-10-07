package me.golf.blog.product.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.blog.commonutils.IntegrationTest
import me.golf.blog.global.jwt.TokenProvider
import me.golf.blog.global.jwt.dto.TokenBaseDto
import me.golf.blog.global.security.CustomUserDetailsService
import me.golf.blog.product.auth.dto.AuthenticationPostRequestDto
import me.golf.blog.product.auth.dto.RefreshPostRequestDto
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberRepository
import me.golf.blog.product.member.util.GivenMember
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
class AuthControllerTest

@Autowired
constructor(
    private val memberRepository: MemberRepository,
    private val objectMapper: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
): IntegrationTest() {

    private lateinit var member: Member
    private lateinit var tokenDto: TokenBaseDto
    private val tokenResponseField = responseFields(
        fieldWithPath("accessToken").description("회원 인증 토큰"),
        fieldWithPath("refreshToken").description("회원 인증 토큰 갱신을 위한 임시 토큰")
    )

    @BeforeEach
    fun setUp() {
        member = memberRepository.save(GivenMember.toMember())

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        tokenDto = tokenProvider.createToken(member.id, authToken)
    }

    @Test
    fun auth() {
        val requestDto = AuthenticationPostRequestDto(
            member.email,
            member.password
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(post("/auths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document("auth/login/request", requestFields(
                    fieldWithPath("email").description("회원 이메일"),
                    fieldWithPath("password").description("회원 비밀번호")
                ))
            )
            .andDo(document("auth/login/response", tokenResponseField))
    }

    @Test
    fun refresh() {
        val requestDto = RefreshPostRequestDto(
            tokenDto.accessToken,
            tokenDto.refreshToken
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(post("/auths/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("auth/refresh/request", requestFields(
                fieldWithPath("accessToken").description("만료된 회원 인증 토큰"),
                fieldWithPath("refreshToken").description("유효한 회원 인증 토큰 갱신을 위한 임시 토큰")
            )))
            .andDo(document("auth/refresh/response", tokenResponseField))
    }
}