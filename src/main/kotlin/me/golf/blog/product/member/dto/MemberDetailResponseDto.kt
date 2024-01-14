package me.golf.blog.product.member.dto

import me.golf.blog.product.member.persist.JobType

data class MemberDetailResponseDto(
    val memberId: Long,
    val email: String,
    val nickname: String,
    val description: String,
    val jobType: JobType,
    val company: String,
    val profileImageUrl: String,
    val experience: Int,
    val followerCount: Long,
    val followCount: Long,
    val boardCount: Long
)
