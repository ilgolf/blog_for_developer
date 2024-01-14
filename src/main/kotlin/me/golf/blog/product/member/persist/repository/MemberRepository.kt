package me.golf.blog.product.member.persist.repository

import me.golf.blog.product.member.persist.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long> {

    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean

    fun existsByIdNotAndNickname(memberId: Long, nickname: String): Boolean
}
