package me.golf.blog.product.post.util

import me.golf.blog.product.post.persist.Post

object GivenPost {

    const val title = "title"
    const val content = "content"
    const val categoryId = 1L
    const val boardId = 1L
    const val postId = 1L

    fun createPost(
        boardId: Long = this.boardId,
        categoryId: Long = this.categoryId,
    ) = Post(
        id = this.postId,
        title = this.title,
        content = this.content,
        categoryId = categoryId,
        boardId = boardId,
    )

    fun createPublishedPost(
        boardId: Long = this.boardId,
        categoryId: Long = this.categoryId,
    ) = Post(
        id = this.postId,
        title = this.title + "published",
        content = this.content + "published",
        categoryId = categoryId,
        boardId = boardId,
    ).apply { publish() }

    fun createPost(
        title: String = this.title,
        content: String = this.content,
        boardId: Long = this.boardId,
        categoryId: Long = this.categoryId,
    ) = Post(
        id = this.postId,
        title = title,
        content = content,
        categoryId = categoryId,
        boardId = boardId,
    )
}