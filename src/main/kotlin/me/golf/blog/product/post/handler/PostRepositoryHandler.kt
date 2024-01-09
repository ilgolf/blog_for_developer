package me.golf.blog.product.post.handler

import me.golf.blog.product.board.handler.BoardRepositoryHandler
import me.golf.blog.product.post.dto.*
import me.golf.blog.product.post.exception.PostException
import me.golf.blog.product.post.persist.Post
import me.golf.blog.product.post.persist.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class PostRepositoryHandler(
    private val postRepository: PostRepository,
    private val boardRepositoryHandler: BoardRepositoryHandler
) {
    fun save(post: Post): Post {

        return post.also { validateTitle(it.title) }
            .also { postRepository.save(it) }
    }

    fun update(handlerDto: PostUpdateHandlerDto): Post {

        return getPost(handlerDto.boardId, handlerDto.memberId, handlerDto.postId) {
            it.modify(handlerDto.title, handlerDto.content)
        }
    }

    fun publish(postId: Long, boardId: Long, memberId: Long): Post {

        return getPost(boardId, memberId, postId) { it.publish() }
    }

    fun delete(requestDto: PostDeleteRequestHandlerDto) {

        getPost(requestDto.boardId, requestDto.memberId, requestDto.postId) { it.delete() }
    }

    fun findById(postId: Long, memberId: Long): PostDetailResponseDto {

        return postRepository.getDetailByIdAndMemberId(postId, memberId)
            ?: throw PostException.PostNotFound()
    }

    fun findSummaryPosts(requestDto: PostSummaryRequestDto): Page<PostSummaryGetResponseDto> {

        return postRepository.findAllBy(requestDto)
    }

    private fun getPost(
        boardId: Long,
        memberId: Long,
        postId: Long,
        block: (Post) -> Unit
    ): Post {

        boardRepositoryHandler.validationExistBoardByIdAndMemberId(boardId, memberId)
            .also { if (!it) throw PostException.PostNotFound() }

        return (postRepository.findByIdAndBoardId(postId, boardId) ?: throw PostException.PostNotFound())
            .also(block)
    }

    private fun validateTitle(title: String) {
        if (postRepository.existsByTitle(title)) {
            throw PostException.PostTitleAlreadyExist()
        }
    }
}
