package me.golf.blog.product.member.dto

import com.querydsl.core.annotations.QueryProjection
import me.golf.blog.product.member.persist.JobType
import java.time.LocalDate

data class MemberDetailResponseDto

@QueryProjection
constructor(
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
