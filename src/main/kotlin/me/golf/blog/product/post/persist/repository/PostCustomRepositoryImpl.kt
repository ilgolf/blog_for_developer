package me.golf.blog.product.post.persist.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.blog.product.board.persist.QBoard.board
import me.golf.blog.product.category.persist.QCategory.category
import me.golf.blog.product.member.persist.QMember.member
import me.golf.blog.product.post.dto.*
import me.golf.blog.product.post.persist.PostSaveStatus
import me.golf.blog.product.post.persist.QPost.post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class PostCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : PostCustomRepository {

    override fun getDetailByIdAndMemberId(postId: Long, memberId: Long): PostDetailResponseDto? {
        return query.select(
            QPostDetailResponseDto(
                post.id,
                post.boardId,
                board.memberId,
                post.title,
                post.content,
                board.title,
                category.name,
                member.name,
                post.createdAt,
                post.lastModifiedDate
            )
        )
            .from(post)
            .innerJoin(board).on(post.boardId.eq(board.id))
            .innerJoin(category).on(post.categoryId.eq(category.id))
            .innerJoin(member).on(board.memberId.eq(member.id))
            .where(post.id.eq(postId))
            .where(member.id.eq(memberId))
            .where(post.postSaveStatus.eq(PostSaveStatus.PUBLISH))
            .fetchOne()
    }

    override fun findAllBy(requestDto: PostSummaryRequestDto): Page<PostSummaryGetResponseDto> {

        val posts = query.select(
            QPostSummaryGetResponseDto(
                post.id,
                post.title,
                post.content,
                post.postMetrics.likeCount,
                post.postMetrics.replyCount,
                post.postMetrics.viewCount,
                post.postSaveStatus.stringValue(),
                post.createdAt.stringValue(),
                post.lastModifiedDate.stringValue(),
                post.categoryId,
                post.boardId,
                member.id,
                category.name
            )).from(post)
            .innerJoin(category).on(post.categoryId.eq(category.id))
            .innerJoin(board).on(post.boardId.eq(board.id))
            .innerJoin(member).on(board.memberId.eq(member.id))
            .where(eqCategoryId(requestDto.categoryId))
            .where(post.boardId.eq(requestDto.boardId))
            .where(post.postSaveStatus.eq(PostSaveStatus.PUBLISH))
            .fetch()

        val totalCount = query.select(post)
            .from(post)
            .innerJoin(category).on(post.categoryId.eq(category.id))
            .innerJoin(board).on(post.boardId.eq(board.id))
            .innerJoin(member).on(board.memberId.eq(member.id))
            .where(eqCategoryId(requestDto.categoryId))
            .where(post.boardId.eq(requestDto.boardId))
            .where(post.postSaveStatus.eq(PostSaveStatus.PUBLISH))
            .fetch()
            .size

        return PageImpl(posts, PageRequest.of(requestDto.page.toInt(), requestDto.size), totalCount.toLong())
    }

    private fun eqCategoryId(categoryId: Long?): BooleanExpression? {
        return categoryId?.let { category.id.eq(categoryId) }
    }
}
