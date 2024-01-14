package me.golf.blog.global.security

import me.golf.blog.global.common.RedisPolicy
import me.golf.blog.product.member.exception.MemberException
import me.golf.blog.product.member.persist.repository.MemberCustomRepository
import me.golf.blog.product.member.persist.repository.MemberRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService(
    private val memberCustomRepository: MemberCustomRepository,
) : UserDetailsService {

    @Transactional(readOnly = true)
    @Cacheable(value = [RedisPolicy.AUTH_KEY], key = "#memberId")
    override fun loadUserByUsername(memberId: String): CustomUserDetails {
        return memberCustomRepository.findAuthInfoById(memberId.toLong())
            ?: throw MemberException.MemberNotFoundException(memberId)
    }
}
