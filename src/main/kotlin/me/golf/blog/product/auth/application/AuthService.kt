package me.golf.blog.product.auth.application

import me.golf.blog.global.jwt.TokenProvider
import me.golf.blog.global.jwt.dto.TokenBaseDto
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.auth.exception.AuthException
import me.golf.blog.product.member.persist.repository.MemberCustomRepository
import me.golf.blog.product.member.persist.repository.MemberRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberCustomRepository: MemberCustomRepository,
    private val tokenProvider: TokenProvider,
    private val managerBuilder: AuthenticationManagerBuilder
) {

    @Transactional
    fun auth(email: String, password: String): TokenBaseDto {
        val userDetails = memberCustomRepository.findAuthInfoByEmail(email)
            ?: throw AuthException.AuthorizationFailException(email)

        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")
        val authentication = managerBuilder.`object`.authenticate(authToken)

        return tokenProvider.createToken(userDetails.memberId, authentication)
    }

    @Transactional
    fun refresh(accessToken: String, refreshToken: String): TokenBaseDto {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw AuthException.RefreshInvalidException()
        }

        val authentication = tokenProvider.getAuthentication(accessToken)
        val userDetails = authentication.principal as CustomUserDetails

        return tokenProvider.createToken(userDetails.memberId, authentication)
    }
}
