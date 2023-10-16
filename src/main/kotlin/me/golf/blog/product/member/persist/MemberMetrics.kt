package me.golf.blog.product.member.persist

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class MemberMetrics(

    @Column(name = "follow_count")
    var followCount: Long,

    @Column(name = "following_count")
    var followingCount: Long,

    @Column(name = "board_count")
    var boardCount: Long
) {

    fun plusBoardCount() {
        this.boardCount ++;
    }
}
