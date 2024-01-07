package me.golf.blog.product.post.application

import me.golf.blog.global.common.PageCustomResponse
import me.golf.blog.product.post.dto.*
import me.golf.blog.product.post.handler.PostRepositoryHandler
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepositoryHandler: PostRepositoryHandler
) : PostUseCase {

    override fun createTemp(requestDto: PostCreateRequestDto): SimplePostResponseDto {

        postRepositoryHandler.save(requestDto.toPost())
            .run { return SimplePostResponseDto(this.id, this.title) }
    }

    override fun update(requestDto: PostUpdateRequestDto): SimplePostResponseDto {

        postRepositoryHandler.update(requestDto.toHandlerDto())
            .run { return SimplePostResponseDto(this.id, this.title) }
    }

    override fun publish(postId: Long, boardId: Long, memberId: Long): SimplePostResponseDto {

        postRepositoryHandler.publish(postId, boardId, memberId)
            .run { return SimplePostResponseDto(this.id, this.title) }
    }

    override fun delete(requestDto: PostDeleteRequestDto) {

        postRepositoryHandler.delete(requestDto.toHandlerDto())
    }

    override fun getById(postId: Long, memberId: Long): PostDetailGetResponseDto {

        postRepositoryHandler.findById(postId, memberId)
            .run { return PostDetailGetResponseDto.of(this) }
    }

    override fun getSummary(requestDto: PostSummaryRequestDto): PageCustomResponse<PostSummaryGetResponseDto> {

        val responsePage = postRepositoryHandler.findSummaryPosts(requestDto)

        return PageCustomResponse.of(responsePage)
    }
}
