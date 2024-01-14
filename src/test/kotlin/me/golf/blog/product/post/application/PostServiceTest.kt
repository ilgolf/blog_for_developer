package me.golf.blog.product.post.application

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import me.golf.blog.product.post.dto.*
import me.golf.blog.product.post.handler.PostRepositoryHandler
import me.golf.blog.product.post.persist.Post
import me.golf.blog.product.post.persist.PostSaveStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.LocalDateTime

@Suppress("NonAsciiCharacters")
class PostServiceTest {

    private lateinit var postService: PostService
    private lateinit var postRepositoryHandler: PostRepositoryHandler
    private lateinit var post: Post

    @BeforeEach
    fun setup() {
        postRepositoryHandler = mockk()
        postService = PostService(postRepositoryHandler)
        post = Post(
            title = "test title",
            content = "test content",
            categoryId = 1L,
            boardId = 1L
        )

        post.id = 1L
    }

    @Test
    fun `게시글을 생성한다`() {
        // given
        val requestDto = PostCreateRequestDto(
            title = "test title",
            content = "test content",
            categoryId = 1L,
            boardId = 1L,
            memberId = 1L
        )

        every { postRepositoryHandler.save(any<Post>()) } returns post

        // when
        postService.createTemp(requestDto)

        // then
        verify(exactly = 1) { postRepositoryHandler.save(any<Post>()) }
    }

    @Test
    fun `게시글을 수정한다`() {
        // given
        val requestDto = PostUpdateRequestDto(
            title = "modify title",
            content = "modify content",
            postId = 1L,
            boardId = 1L,
            memberId = 1L
        )

        every { postRepositoryHandler.update(any<PostUpdateHandlerDto>()) } returns post

        // when
        postService.update(requestDto)

        // then
        verify(exactly = 1) { postRepositoryHandler.update(any<PostUpdateHandlerDto>()) }
    }

    @Test
    fun `게시글을 발행한다`() {
        // given
        val postId = 1L
        val memberId = 1L
        val boardId = 1L

        every { postRepositoryHandler.publish(any<Long>(), any<Long>(), any<Long>()) } returns post

        // when
        postService.publish(postId = postId, boardId = boardId, memberId = memberId)

        // then
        verify(exactly = 1) { postRepositoryHandler.publish(any<Long>(), any<Long>(), any<Long>()) }
    }

    @Test
    fun `게시글을 삭제한다`() {
        // given
        val requestDto = PostDeleteRequestDto(
            postId = 1L,
            boardId = 1L,
            memberId = 1L
        )

        justRun { postRepositoryHandler.delete(any()) }

        // when
        postService.delete(requestDto)

        // then
        verify(exactly = 1) { postRepositoryHandler.delete(any()) }
    }

    @Test
    fun `게시글을 조회한다`() {
        // given
        val postId = 1L
        val memberId = 1L

        val postDetail = PostDetailResponseDto(
            postId = 1L,
            boardId = 1L,
            memberId = 1L,
            title = "test title",
            content = "test content",
            categoryName = "test category",
            boardName = "test board",
            memberName = "test member",
            createdDate = LocalDateTime.now(),
            updatedDate = LocalDateTime.now()
        )

        every { postRepositoryHandler.findById(any<Long>(), any<Long>()) } returns postDetail

        // when
        postService.getById(postId, memberId)

        // then
        verify(exactly = 1) {
            postRepositoryHandler.findById(any<Long>(), any<Long>())
        }
    }

    @Test
    fun `게시글 목록을 조회한다`() {
        // given
        val requestDto = PostSummaryRequestDto(
            boardId = 1L,
            categoryId = 1L,
            memberId = 1L,
            page = 0,
            size = 10
        )

        val postSummary = PostSummaryGetResponseDto(
            id = 1L,
            boardId = 1L,
            memberId = 1L,
            categoryId = 1L,
            title = "test title",
            content = "test content",
            categoryName = "test category",
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now(),
            likeCount = 1,
            replyCount = 1,
            viewCount = 1,
            postSaveStatus = PostSaveStatus.PUBLISH,
        )

        val pageResponse = PageImpl(listOf(postSummary))

        every { postRepositoryHandler.findSummaryPosts(any()) } returns pageResponse

        // when
        postService.getSummary(requestDto)

        // then
        verify(exactly = 1) {
            postRepositoryHandler.findSummaryPosts(any())
        }
    }
}