package me.golf.blog.product.member.dto

import me.golf.blog.product.member.persist.JobType

data class MemberUpdateHandlerRequestDto(
    val nickname: String,
    val name: String,
    val description: String,
    val jobType: JobType,
    val company: String,
    val profileImageUrl: String,
    val experience: Int,
    val memberId: Long
)
