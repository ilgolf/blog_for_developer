package me.golf.blog.product.member.persist.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.global.security.QCustomUserDetails
import me.golf.blog.product.member.dto.MemberDetailResponseDto
import me.golf.blog.product.member.dto.QMemberDetailResponseDto
import me.golf.blog.product.member.persist.QMember.member

class MemberCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : MemberCustomRepository {

    override fun getAuthInfoById(memberId: Long): CustomUserDetails? {
        return query.select(
            QCustomUserDetails(
                member.id,
                member.email,
                member.role
            )
        )
            .from(member)
            .where(member.id.eq(memberId))
            .fetchOne()
    }

    override fun getAuthInfoByEmail(email: String): CustomUserDetails? {
        return query.select(
            QCustomUserDetails(
                member.id,
                member.email,
                member.role
            )
        )
            .from(member)
            .where(member.email.eq(email))
            .fetchOne()
    }

    override fun getDetailInfo(memberId: Long): MemberDetailResponseDto? {
        return query.select(
            QMemberDetailResponseDto(
                member.id,
                member.email,
                member.nickname,
                member.description,
                member.jobType,
                member.company,
                member.profileImageUrl,
                member.experience,
                member.memberMetrics.followCount,
                member.memberMetrics.followingCount,
                member.memberMetrics.boardCount
            ))
            .from(member)
            .where(member.activated.isTrue)
            .fetchOne()
    }
}