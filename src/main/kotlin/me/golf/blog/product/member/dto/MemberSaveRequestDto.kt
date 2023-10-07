package me.golf.blog.product.member.dto

import me.golf.blog.product.member.persist.Member
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate

data class MemberSaveRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    val birth: LocalDate
) {
    fun createMember(passwordEncoder: PasswordEncoder): Member {
        return Member(
            email = this.email,
            password = passwordEncoder.encode(this.password),
            name = this.name,
            nickname = this.nickname,
            birth = this.birth
        )
    }
}
