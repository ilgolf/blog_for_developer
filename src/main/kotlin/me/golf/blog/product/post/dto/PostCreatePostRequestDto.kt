package me.golf.blog.product.post.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.golf.blog.global.common.Min

data class PostCreatePostRequestDto(

    @field:NotBlank(message = "제목은 필수 값입니다.")
    @field:Size(min = 5, max = 50, message = "제목은 최소 5자 최대 50자입니다. 규격을 지켜주세요.")
    val title: String,

    @field:NotNull(message = "내용에 null 값은 올 수 없습니다.")
    val content: String,

    @field:Min(min = 0, message = "카테고리 식별자는 0보다 커야합니다.")
    val categoryId: Long,

    @field:NotNull(message = "게시판 식별자는 필수 값입니다.")
    @field:Min(min = 0, message = "게시판 식별자는 0보다 커야합니다.")
    val boardId: Long
) {

    fun toServiceDto(memberId: Long): PostCreateRequestDto {

        return PostCreateRequestDto(
            title = this.title,
            content = this.content,
            categoryId = this.categoryId,
            boardId = this.boardId,
            memberId = memberId
        )
    }
}
