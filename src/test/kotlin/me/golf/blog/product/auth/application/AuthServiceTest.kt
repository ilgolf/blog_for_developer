package me.golf.blog.product.auth.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.golf.blog.global.jwt.TokenProvider
import me.golf.blog.global.jwt.dto.TokenBaseDto
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.auth.exception.AuthException
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberCustomRepository
import me.golf.blog.product.member.persist.repository.MemberRepository
import me.golf.blog.product.member.util.GivenMember
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder

class AuthServiceTest {

    private val memberRepository = mockk<MemberRepository>()
    private val memberCustomRepository = mockk<MemberCustomRepository>()
    private val tokenProvider = mockk<TokenProvider>()
    private val managerBuilder = mockk<AuthenticationManagerBuilder>()

    private lateinit var authService: AuthService
    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        member = GivenMember.toMember()
        authService = AuthService(memberCustomRepository, tokenProvider, managerBuilder)
    }

    @Test
    fun `인증에 성공하고 AccessToken을 발급 받는다`() {
        // given
        val userDetails = CustomUserDetails.of(member)
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")
        val tokenBaseDto = TokenBaseDto("accessToken", "refreshToken")

        every { memberCustomRepository.findAuthInfoByEmail(any()) } returns userDetails
        every { managerBuilder.`object`.authenticate(any()) } returns authToken
        every { tokenProvider.createToken(any(), any()) } returns tokenBaseDto

        // when
        val accessToken = authService.auth(GivenMember.EMAIL, GivenMember.PASSWORD).accessToken

        // then
        assertThat(accessToken).isEqualTo("accessToken")
        verify { memberCustomRepository.findAuthInfoByEmail(any()) }
    }

    @Test
    fun `인증에 실패하면 인증에 실패했습니다 예외 발생`() {
        // given

        every { memberCustomRepository.findAuthInfoByEmail(any()) } returns null

        // when
        val exception = catchException { authService.auth(GivenMember.EMAIL, GivenMember.PASSWORD).accessToken }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(AuthException.AuthorizationFailException::class.java) },
            { assertThat(exception.message).isEqualTo("인증에 실패했습니다.: ${GivenMember.EMAIL}") }
        )
        verify(exactly = 0) { tokenProvider.createToken(any(), any()) }
        verify(exactly = 0) { managerBuilder.`object`.authenticate(any()) }
    }

    @Test
    fun `유효한 refreshToken을 갖고 있다면 AccessToken을 재발급 받을 수 있다`() {
        // given
        val userDetails = CustomUserDetails.of(member)
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")
        val originAccessToken = "accessToken"
        val originRefreshToken = "refreshToken"

        val newBaseDto = TokenBaseDto("newAccessToken", "newRefreshToken")

        every { tokenProvider.validateToken(any()) } returns true
        every { tokenProvider.getAuthentication(any()) } returns authToken
        every { tokenProvider.createToken(any(), any()) } returns newBaseDto

        // when
        val tokenBaseDto = authService.refresh(originAccessToken, originRefreshToken)

        // then
        assertThat(tokenBaseDto.accessToken).isEqualTo("newAccessToken")
        verify { tokenProvider.createToken(any(), any()) }
    }

    @Test
    fun `유효한 refreshToken이 아니라면 리프레시 토큰 인증에 실패했습니다 예외 발생`() {
        // given
        val originAccessToken = "accessToken"
        val originRefreshToken = "refreshToken"

        every { tokenProvider.validateToken(any()) } returns false

        // when
        val exception = catchException { authService.refresh(originAccessToken, originRefreshToken) }

        // then
        assertAll(
            { assertThat(exception).isInstanceOf(AuthException.RefreshInvalidException::class.java) },
            { assertThat(exception.message).isEqualTo("리프레시 토큰 인증에 실패했습니다.: ") }
        )
        verify(exactly = 0) { tokenProvider.createToken(any(), any()) }
        verify(exactly = 0) { tokenProvider.getAuthentication(any()) }
    }
}