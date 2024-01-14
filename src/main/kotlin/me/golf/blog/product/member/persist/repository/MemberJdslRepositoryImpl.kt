package me.golf.blog.product.member.persist.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import me.golf.blog.global.config.createQuery
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.member.dto.MemberDetailResponseDto
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.MemberMetrics
import org.springframework.stereotype.Repository

@Repository
class MemberJdslRepositoryImpl(
    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
    private val jpqlRenderer: JpqlRenderer,
): MemberCustomRepository {

    override fun findAuthInfoById(id: Long): CustomUserDetails? {
        val query = jpql {
            selectNew<CustomUserDetails>(
                path(Member::id).`as`(expression(Long::class, "memberId")),
                path(Member::email),
                path(Member::role)
            )
                .from(entity(Member::class))
                .where(path(Member::id).eq(id))
        }

        return entityManager.createQuery(query, jpqlRenderContext, jpqlRenderer)
    }

    override fun findAuthInfoByEmail(email: String): CustomUserDetails? {
        val query = jpql {
            selectNew<CustomUserDetails>(
                path(Member::id).`as`(expression(Long::class, "memberId")),
                path(Member::email),
                path(Member::role)
            )
                .from(entity(Member::class))
                .where(path(Member::email).eq(email))
        }

        return entityManager.createQuery(query, jpqlRenderContext, jpqlRenderer)
    }

    override fun findDetailById(id: Long): MemberDetailResponseDto? {
        val query = jpql {
            selectNew<MemberDetailResponseDto>(
                path(Member::id).`as`(expression(Long::class, "memberId")),
                path(Member::email),
                path(Member::nickname),
                path(Member::description),
                path(Member::jobType),
                path(Member::company),
                path(Member::profileImageUrl),
                path(Member::experience),
                path(Member::memberMetrics).path(MemberMetrics::followCount).`as`(expression(Long::class, "followerCount")),
                path(Member::memberMetrics).path(MemberMetrics::followingCount).`as`(expression(Long::class, "followCount")),
                path(Member::memberMetrics).path(MemberMetrics::boardCount),
            )
                .from(entity(Member::class))
        }

        return entityManager.createQuery(query, jpqlRenderContext, jpqlRenderer)
    }
}
