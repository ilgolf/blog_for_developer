package me.golf.blog.product.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import me.golf.blog.product.member.persist.JobType
import me.golf.blog.product.member.validation.ValidJobType
import org.springframework.validation.BindingResult
import java.time.LocalDate

data class MemberSavePostRequestDto(

    @field:NotBlank(message = "이메일은 필수 값입니다.")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$",
        message = "이메일 형식이 아닙니다."
    )
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 값입니다.")
    val password: String,

    @field:NotBlank(message = "비밀번호 확인을 꼭 입력해주셔야 합니다.")
    val passwordConfirm: String,

    @field:NotBlank(message = "이름은 필수 값입니다.")
    @field:Size(max = 30, message = "이름은 최대 30자까지 입력 가능합니다.")
    val name: String,

    @field:NotBlank(message = "닉네임은 필수 값입니다.")
    @field:Size(max = 30, message = "닉네임은 최대 30자까지 입력 가능합니다.")
    val nickname: String,

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

    fun toServiceDto(): MemberSaveRequestDto {
        return MemberSaveRequestDto(
            email = this.email,
            password = this.password,
            name = this.name,
            nickname = this.nickname,
            description = this.description?: "",
            jobType = JobType.valueOf(this.jobType),
            company = this.company,
            experience = this.experience,
            profileImageUrl = this.profileImageUrl?: "",
        )
    }

    fun matchPasswordAndConfirm() = this.passwordConfirm == this.password
}
