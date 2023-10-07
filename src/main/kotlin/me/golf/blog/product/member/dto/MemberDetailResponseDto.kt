package me.golf.blog.product.member.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class MemberDetailResponseDto

@QueryProjection
constructor(
    val memberId: Long,
    val email: String,
    val nickname: String,
    val birth: LocalDate,
    val followerCount: Int,
    val followCount: Int,
    val boardCount: Int
)
