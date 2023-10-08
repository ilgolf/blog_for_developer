package me.golf.blog.product.member.dto

import me.golf.blog.product.member.persist.JobType
import me.golf.blog.product.member.persist.Member
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate

data class MemberSaveRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    val description: String,
    val jobType: JobType,
    val company: String,
    val experience: Int,
    val profileImageUrl: String
) {
    fun createMember(passwordEncoder: PasswordEncoder): Member {
        return Member(
            email = this.email,
            password = passwordEncoder.encode(this.password),
            name = this.name,
            nickname = this.nickname,
            description = this.description,
            jobType = this.jobType,
            company = this.company,
            experience = this.experience,
            profileImageUrl = this.profileImageUrl,
        )
    }
}
