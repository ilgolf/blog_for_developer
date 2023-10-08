package me.golf.blog.product.member.dto

import me.golf.blog.product.member.persist.JobType
import java.time.LocalDate

data class MemberDetailGetResponseDto(
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
) {

    companion object {

        fun of(responseDto: MemberDetailResponseDto): MemberDetailGetResponseDto {
            return MemberDetailGetResponseDto(
                responseDto.memberId,
                responseDto.email,
                responseDto.nickname,
                responseDto.description,
                responseDto.jobType,
                responseDto.company,
                responseDto.profileImageUrl,
                responseDto.experience,
                responseDto.followerCount,
                responseDto.followCount,
                responseDto.boardCount
            )
        }
    }
}
