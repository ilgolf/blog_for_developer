package me.golf.blog.product.member.handler

import me.golf.blog.product.member.dto.MemberDetailResponseDto
import me.golf.blog.product.member.exception.MemberException
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MemberRepositoryHandler(
    private val memberRepository: MemberRepository
) {

    fun saveProcess(memberEntity: Member): Member {

        validationExistBeforeSave(memberEntity.nickname, memberEntity.email)
        return memberRepository.save(memberEntity)
    }

    fun beforeUpdateProcess(memberId: Long, nickname: String): Member {

        validationExistBeforeUpdate(memberId, nickname)
        return getMember(memberId)
    }

    fun beforeWithdrawProcess(memberId: Long): Member {

        return getMember(memberId)
    }

    fun getOneProcess(memberId: Long): MemberDetailResponseDto {

        return memberRepository.getDetailInfo(memberId)
            ?: throw MemberException.MemberNotFoundException(memberId.toString())
    }

    fun getMember(memberId: Long): Member {

        return memberRepository.findByIdOrNull(memberId)
            ?: throw MemberException.MemberNotFoundException(memberId.toString())
    }

    private fun validationExistBeforeSave(nickname: String, email: String) {

        validateIfExists(memberRepository.existsByNickname(nickname), nickname)
        validateIfExists(memberRepository.existsByEmail(email), email)
    }

    private fun validationExistBeforeUpdate(memberId: Long, nickname: String) {

        validateIfExists(memberRepository.existsByIdNotAndNickname(memberId, nickname), nickname)
    }

    private fun validateIfExists(exists: Boolean, value: String) {

        if (exists) {
            throw MemberException.AlreadyExistException(value)
        }
    }
}
