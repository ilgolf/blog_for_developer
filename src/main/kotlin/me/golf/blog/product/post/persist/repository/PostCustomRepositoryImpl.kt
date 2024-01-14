package me.golf.blog.product.post.persist.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import me.golf.blog.global.config.createQuery
import me.golf.blog.global.config.createQueryCount
import me.golf.blog.global.config.createQueryList
import me.golf.blog.product.board.persist.Board
import me.golf.blog.product.category.persist.Category
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.post.dto.PostDetailResponseDto
import me.golf.blog.product.post.dto.PostSummaryGetResponseDto
import me.golf.blog.product.post.dto.PostSummaryRequestDto
import me.golf.blog.product.post.persist.Post
import me.golf.blog.product.post.persist.PostMetrics
import me.golf.blog.product.post.persist.PostSaveStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PostCustomRepositoryImpl(
    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
    private val jpqlRenderer: JpqlRenderer,
) : PostCustomRepository {

    override fun getDetailByIdAndMemberId(postId: Long, memberId: Long): PostDetailResponseDto? {
        val query = jpql {
            selectNew<PostDetailResponseDto>(
                path(Post::id).`as`(expression(Long::class, "postId")),
                path(Post::boardId),
                path(Board::memberId),
                path(Post::title),
                path(Post::content),
                path(Board::title).`as`(expression(String::class, "boardName")),
                path(Category::name).`as`(expression(String::class, "categoryName")),
                path(Member::name).`as`(expression(String::class, "memberName")),
                path(Post::createdAt),
                path(Post::lastModifiedDate).`as`(expression(LocalDateTime::class, "updatedAt")),
            )
                .from(
                    entity(Post::class),
                    join(Board::class).on(path(Post::boardId).eq(path(Board::id))),
                    join(Category::class).on(path(Post::categoryId).eq(path(Category::id))),
                    join(Member::class).on(path(Board::memberId).eq(path(Member::id)))
                )
                .where(
                    path(Post::id).eq(postId)
                        .and(path(Member::id).eq(memberId))
                        .and(path(Post::postSaveStatus).eq(PostSaveStatus.PUBLISH))
                )
        }

        return entityManager.createQuery(query, jpqlRenderContext, jpqlRenderer)
    }

    override fun findAllBy(requestDto: PostSummaryRequestDto): Page<PostSummaryGetResponseDto> {
        val query = jpql {
            selectNew<PostSummaryGetResponseDto>(
                path(Post::id).`as`(expression(Long::class, "id")),
                path(Post::title),
                path(Post::content),
                path(Post::postMetrics).path(PostMetrics::likeCount),
                path(Post::postMetrics).path(PostMetrics::replyCount),
                path(Post::postMetrics).path(PostMetrics::viewCount),
                path(Post::postSaveStatus),
                path(Post::createdAt).`as`(expression(LocalDateTime::class, "createdDate")),
                path(Post::lastModifiedDate).`as`(expression(LocalDateTime::class, "lastModifiedDate")),
                path(Post::categoryId),
                path(Post::boardId),
                path(Board::memberId),
                path(Category::name).`as`(expression(String::class, "categoryName")),
            )
                .from(
                    entity(Post::class),
                    join(Board::class).on(path(Post::boardId).eq(path(Board::id))),
                    join(Category::class).on(path(Post::categoryId).eq(path(Category::id))),
                )
                .where(
                    path(Post::postSaveStatus).eq(PostSaveStatus.PUBLISH)
                        .and(path(Post::boardId).eq(requestDto.boardId))
                        .and(path(Post::categoryId).eq(requestDto.categoryId))
                )
        }

        val countQuery = jpql {
            select(count(Post::id))
                .from(
                    entity(Post::class),
                    join(Board::class).on(path(Post::boardId).eq(path(Board::id))),
                    join(Category::class).on(path(Post::categoryId).eq(path(Category::id))),
                )
                .where(
                    path(Post::postSaveStatus).eq(PostSaveStatus.PUBLISH)
                        .and(path(Post::boardId).eq(requestDto.boardId))
                        .and(path(Post::categoryId).eq(requestDto.categoryId))
                )
        }

        return PageImpl(
            entityManager.createQueryList(query, jpqlRenderContext, jpqlRenderer),
            PageRequest.of(requestDto.page.toInt(), requestDto.size),
            entityManager.createQueryCount<Long>(countQuery, jpqlRenderContext, jpqlRenderer)
        )
    }
}
