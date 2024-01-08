package me.golf.blog.product.board.persist

import jakarta.persistence.*
import me.golf.blog.global.common.BaseEntity
import org.hibernate.annotations.Filter

@Entity
@Filter(name = "deletedFilter", condition = "deleted = false")
class Board(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", updatable = false, unique = true)
    var id: Long = 0L,

    @Column(name = "title", unique = true)
    var title: String,

    @Column(name = "description")
    var description: String,

    @Column(name = "post_url", unique = true, updatable = false)
    var boardUrl: String,

    @Column(name = "member_id", updatable = false)
    var memberId: Long,

    @Embedded
    var boardMetrics: BoardMetrics = BoardMetrics(0, 0),

    @Column(name = "deleted")
    var deleted: Boolean = false

) : BaseEntity() {

    fun update(title: String, description: String) {
        this.title = title
        this.description = description
    }

    fun delete() {
        this.deleted = true
    }
}