package me.golf.blog.product.post.persist.repository

import me.golf.blog.product.post.persist.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>, PostCustomRepository {

    fun existsByTitle(title: String): Boolean
    fun findByIdAndBoardId(postId: Long, boardId: Long): Post?
}
