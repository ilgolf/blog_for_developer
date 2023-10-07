package me.golf.blog.product.member.application

import me.golf.blog.global.common.RedisPolicy
import me.golf.blog.product.member.dto.MemberDetailResponseDto
import me.golf.blog.product.member.dto.MemberSaveRequestDto
import me.golf.blog.product.member.dto.MemberUpdateRequestDto
import me.golf.blog.product.member.dto.SimpleMemberResponseDto
import me.golf.blog.product.member.handler.MemberRepositoryHandler
import org.springframework.cache.annotation.CacheEvict
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepositoryHandler: MemberRepositoryHandler,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun save(requestDto: MemberSaveRequestDto): SimpleMemberResponseDto {

        val member = requestDto.createMember(passwordEncoder)
        val saveMember = memberRepositoryHandler.saveProcess(member)

        return SimpleMemberResponseDto(memberId = saveMember.id, email = saveMember.email)
    }

    @Transactional
    fun update(requestDto: MemberUpdateRequestDto): SimpleMemberResponseDto {

        val updatedMember = memberRepositoryHandler.beforeUpdateProcess(requestDto.memberId, requestDto.nickname)
            .also { it.update(requestDto) }

        return SimpleMemberResponseDto(memberId = updatedMember.id, email =  updatedMember.email)
    }

    @Transactional
    @CacheEvict(value = [RedisPolicy.AUTH_KEY], key = "#memberId")
    fun withdraw(memberId: Long) {

        memberRepositoryHandler.beforeWithdrawProcess(memberId).withdraw()
    }

    @Transactional(readOnly = true)
    fun getDetail(memberId: Long): MemberDetailResponseDto {

        return memberRepositoryHandler.getOneProcess(memberId)
    }
}
