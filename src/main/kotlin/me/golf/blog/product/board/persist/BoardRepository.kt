package me.golf.blog.product.board.persist

import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long> {

    fun findByIdAndMemberId(boardId: Long, memberId: Long): Board?

    fun existsByBoardUrl(boardUrl: String): Boolean

    fun existsByTitle(title: String): Boolean
    fun countByMemberId(memberId: Long): Long
    fun findByMemberIdOrderByIdDesc(memberId: Long): List<Board>
}
