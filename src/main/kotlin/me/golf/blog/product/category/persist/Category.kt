package me.golf.blog.product.category.persist

import jakarta.persistence.*
import me.golf.blog.global.common.BaseEntity

@Entity
class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L,

    @Column(name = "name")
    var name: String,

    @Column(name = "parentId")
    var parentId: Long?, // null 이면 최상위 카테고리

    @Column(name = "orders")
    var order: Int,

    @Column(name = "isUsed")
    var isUsed: Boolean

): BaseEntity() {

    fun modify(name: String, order: Int) {
        this.name = name
        this.order = order
    }

    fun use() {
        this.isUsed = true
    }

    fun unUse() {
        this.isUsed = false
    }

    fun changeParent(parentId: Long) {
        this.parentId = parentId
    }
}
