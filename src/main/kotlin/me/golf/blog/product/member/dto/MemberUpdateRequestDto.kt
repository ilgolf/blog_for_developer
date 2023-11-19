package me.golf.blog.product.member.dto

import me.golf.blog.product.member.persist.JobType

data class MemberUpdateRequestDto(
    val nickname: String,
    val name: String,
    val description: String,
    val jobType: JobType,
    val company: String,
    val profileImageUrl: String,
    val experience: Int,
    val memberId: Long
) {

    fun toHandlerDto(): MemberUpdateHandlerRequestDto {
        return MemberUpdateHandlerRequestDto(
            nickname = this.nickname,
            name = this.name,
            description = this.description,
            jobType = this.jobType,
            company = this.company,
            profileImageUrl = this.profileImageUrl,
            experience = this.experience,
            memberId = this.memberId
        )
    }
}
