package me.golf.blog.product.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class MemberUpdatePutRequestDto(

    @NotBlank(message = "별칭은 필수 값입니다.")
    val nickname: String,

    @NotBlank(message = "이름은 필수 값입니다.")
    val name: String,

    @NotBlank(message = "생일은 필수 값입니다.")
    @field:Pattern(regexp = "^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$")
    val birth: String
) {

    fun toServiceDto(memberId: Long): MemberUpdateRequestDto {
        return MemberUpdateRequestDto(
            nickname = this.nickname,
            name = this.name,
            birth = LocalDate.parse(this.birth),
            memberId = memberId
        )
    }
}