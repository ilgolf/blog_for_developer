package me.golf.blog.product.board.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class BoardCreatePostRequestDto(

    @field:NotBlank(message = "제목은 필수 값입니다.")
    @field:Size(max = 50, message = "게시판 제목은 50자가 최대입니다.")
    val title: String,

    @field:Size(max = 255, message = "자기 소개에 대한 글은 255자가 최대입니다.")
    val description: String,

    @field:NotBlank(message = "게시판 url은 필수 값입니다.")
    @field:Size(max = 50, message = "게시판 url은 50자가 최대입니다.")
    val boardUrl: String
) {

    fun toService(memberId: Long): BoardCreateRequestDto {
        return BoardCreateRequestDto(
            title = this.title,
            description = this.description,
            boardUrl = this.boardUrl,
            memberId = memberId
        )
    }
}
