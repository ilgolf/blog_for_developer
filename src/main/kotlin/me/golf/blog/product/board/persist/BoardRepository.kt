package me.golf.blog.product.board.persist

import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long> {

    fun existsByPostUrl(postUrl: String): Boolean

    fun existsByTitle(title: String): Boolean
}
