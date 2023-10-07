package me.golf.blog.product.member.persist

import jakarta.persistence.*
import me.golf.blog.global.common.BaseEntity
import me.golf.blog.global.common.BaseTimeEntity
import me.golf.blog.product.member.dto.MemberUpdateRequestDto
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate

@Entity
@DynamicUpdate
class Member(

    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "nickname", unique = true)
    var nickname: String,

    @Column(name = "birth")
    var birth: LocalDate
): BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "activated")
    var activated: Boolean = true

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = Role.USER

    @Embedded
    var memberMetrics: MemberMetrics = MemberMetrics(0, 0, 0)

    fun update(requestDto: MemberUpdateRequestDto) {
        this.nickname = requestDto.nickname
        this.name = requestDto.name
        this.birth = requestDto.birth
    }

    fun withdraw() {
        this.activated = false
    }
}