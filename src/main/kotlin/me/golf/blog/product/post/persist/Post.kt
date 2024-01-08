package me.golf.blog.product.post.persist

import jakarta.persistence.*
import me.golf.blog.global.common.BaseEntity
import me.golf.blog.product.category.persist.Category
import org.hibernate.annotations.Filter

@Entity
@Filter(name = "deletedFilter", condition = "deleted = false")
class Post(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    var id: Long = 0L,

    @Column(name = "title", unique = true)
    var title:String,

    @Column(name = "content", columnDefinition = "TEXT")
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "post_save_status")
    var postSaveStatus: PostSaveStatus = PostSaveStatus.TEMP,

    @Column(name = "board_id")
    var boardId: Long,

    @Column(name = "category_id")
    var categoryId: Long,

    @Embedded
    var postMetrics: PostMetrics = PostMetrics(likeCount = 0, replyCount = 0, viewCount = 0),

    @Column(name = "deleted")
    var deleted: Boolean = false
): BaseEntity() {

    fun modify(title: String, content: String) {
        this.title = title
        this.content = content
    }

    fun delete() {
        this.deleted = true
    }

    fun publish() {
        this.postSaveStatus = PostSaveStatus.PUBLISH
    }
}
