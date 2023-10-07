package me.golf.blog.product.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
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

    @field:NotBlank(message = "생년월일은 필수 값입니다.")
    @field:Pattern(
        regexp = "^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$",
        message = "날짜 형식이 아닙니다."
    )
    val birth: String
) {

    fun toServiceDto(): MemberSaveRequestDto {
        return MemberSaveRequestDto(
            email = this.email,
            password = this.password,
            name = this.name,
            nickname = this.nickname,
            birth = LocalDate.parse(this.birth)
        )
    }

    fun matchPasswordAndConfirm() = this.passwordConfirm == this.password
}
