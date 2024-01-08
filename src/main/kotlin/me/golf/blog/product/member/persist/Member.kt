package me.golf.blog.product.member.persist

import jakarta.persistence.*
import me.golf.blog.global.common.BaseTimeEntity
import me.golf.blog.product.member.dto.MemberUpdateHandlerRequestDto
import me.golf.blog.product.member.dto.MemberUpdateRequestDto
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Filter

@Entity
@DynamicUpdate
@Filter(name = "activatedFilter", condition = "activated = true")
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var id: Long = 0L,

    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "nickname", unique = true)
    var nickname: String,

    @Column(name = "description")
    var description: String,

    @Column(name = "job")
    var jobType: JobType,

    @Column(name = "company")
    var company: String,

    @Column(name = "experience")
    var experience: Int,

    @Column(name = "profile_image_url", nullable = true)
    var profileImageUrl: String,

    @Column(name = "activated")
    var activated: Boolean = true,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = Role.USER,

    @Embedded
    var memberMetrics: MemberMetrics = MemberMetrics(0, 0, 0)

) : BaseTimeEntity() {

    fun update(requestDto: MemberUpdateHandlerRequestDto) {
        this.nickname = requestDto.nickname
        this.name = requestDto.name
        this.description = requestDto.description
        this.jobType = requestDto.jobType
        this.company = requestDto.company
        this.profileImageUrl = requestDto.profileImageUrl
        this.experience = requestDto.experience
    }

    fun withdraw() {
        this.activated = false
    }
}