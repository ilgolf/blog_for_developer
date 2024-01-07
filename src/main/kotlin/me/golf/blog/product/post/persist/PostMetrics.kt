package me.golf.blog.product.post.persist

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class PostMetrics(

    @Column(name = "like_count")
    var likeCount: Long,

    @Column(name = "replyCount")
    var replyCount: Long,

    @Column(name = "view_count")
    var viewCount: Long
)
