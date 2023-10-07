package me.golf.blog.product.board.persist

import jakarta.persistence.Embeddable

@Embeddable
class BoardMetrics(
    val postCount: Int,
    val viewCount: Int
)
