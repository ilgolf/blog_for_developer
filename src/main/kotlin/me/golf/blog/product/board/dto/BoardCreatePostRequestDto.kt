package me.golf.blog.product.board.dto

data class BoardCreatePostRequestDto(
    val title: String,
    val description: String,
    val postUrl: String
) {

    fun toService(memberId: Long): BoardCreateRequestDto {
        return BoardCreateRequestDto(
            title = this.title,
            description = this.description,
            postUrl = this.postUrl,
            memberId = memberId
        )
    }
}
