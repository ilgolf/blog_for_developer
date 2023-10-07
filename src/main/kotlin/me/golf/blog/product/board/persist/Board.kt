package me.golf.blog.product.board.persist

import jakarta.persistence.*
import me.golf.blog.global.common.BaseEntity

@Entity
class Board(

    @Column(name = "title", unique = true)
    var title: String,

    @Column(name = "description")
    var description: String,

    @Column(name = "post_url", unique = true, updatable = false)
    var postUrl: String,

    @Column(name = "member_id", updatable = false)
    var memberId: Long
): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", updatable = false, unique = true)
    var id: Long = 0L

    @Embedded
    var boardMetrics: BoardMetrics = BoardMetrics(0, 0)

    fun update(title: String, description: String) {
        this.title = title
        this.description = description
    }
}