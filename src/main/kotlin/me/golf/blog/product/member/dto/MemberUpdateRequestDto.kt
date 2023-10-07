package me.golf.blog.product.member.dto

import java.time.LocalDate

data class MemberUpdateRequestDto(
    val nickname: String,
    val name: String,
    val birth: LocalDate,
    val memberId: Long
)
