package me.golf.blog.product.member.dto

import java.time.LocalDate

data class MemberDetailGetResponseDto(
    val memberId: Long,
    val email: String,
    val nickname: String,
    val birth: LocalDate,
    val followerCount: Int,
    val followCount: Int,
    val boardCount: Int
) {

    companion object {

        fun of(responseDto: MemberDetailResponseDto): MemberDetailGetResponseDto {
            return MemberDetailGetResponseDto(
                responseDto.memberId,
                responseDto.email,
                responseDto.nickname,
                responseDto.birth,
                responseDto.followerCount,
                responseDto.followCount,
                responseDto.boardCount
            )
        }
    }
}
