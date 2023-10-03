package me.golf.blog.global.common

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@MappedSuperclass
open class BaseEntity(

    @CreatedBy
    @Column(updatable = false)
    open var createdBy: Long = 0L,

    @LastModifiedBy
    open var lastModifiedBy: Long = 0L
): BaseTimeEntity()