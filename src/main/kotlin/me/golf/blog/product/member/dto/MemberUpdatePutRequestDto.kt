package me.golf.blog.product.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.golf.blog.product.member.persist.JobType
import me.golf.blog.product.member.validation.ValidJobType

data class MemberUpdatePutRequestDto(

    @NotBlank(message = "별칭은 필수 값입니다.")
    val nickname: String,

    @NotBlank(message = "이름은 필수 값입니다.")
    val name: String,

    @field:Size(max = 255, message = "자기 소개는 최대 255자 까지 입력 가능합니다.")
    val description: String?,

    @field:NotBlank(message = "직업은 필수 값입니다.")
    @field:ValidJobType(message = "직업 타입에 맞지 않는 타입입니다.")
    val jobType: String,

    @field:NotBlank(message = "회사 명은 필수 값입니다.")
    val company: String,

    @field:NotNull(message = "연차는 필수입니다.")
    val experience: Int,

    @field:Size(max = 255, message = "프로필 url 정보는 최대 255자까지 입력 가능합니다.")
    val profileImageUrl: String?
) {

    fun toServiceDto(memberId: Long): MemberUpdateRequestDto {
        return MemberUpdateRequestDto(
            nickname = this.nickname,
            name = this.name,
            description = this.description?: "",
            jobType = JobType.valueOf(this.jobType),
            company = this.company,
            profileImageUrl = this.profileImageUrl?: "",
            experience = this.experience,
            memberId = memberId
        )
    }
}