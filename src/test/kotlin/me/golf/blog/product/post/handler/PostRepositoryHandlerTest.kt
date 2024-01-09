package me.golf.blog.product.post.handler

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.golf.blog.product.board.handler.BoardRepositoryHandler
import me.golf.blog.product.post.dto.*
import me.golf.blog.product.post.exception.PostException
import me.golf.blog.product.post.persist.Post
import me.golf.blog.product.post.persist.PostSaveStatus
import me.golf.blog.product.post.persist.repository.PostRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.data.domain.PageImpl
import java.time.LocalDateTime

@Suppress("NonAsciiCharacters")
class PostRepositoryHandlerTest {

    private lateinit var postRepositoryHandler: PostRepositoryHandler
    private lateinit var postRepository: PostRepository
    private lateinit var boardRepositoryHandler: BoardRepositoryHandler
    private lateinit var post: Post

    @BeforeEach
    fun setUp() {
        postRepository = mockk(relaxed = true)
        boardRepositoryHandler = mockk(relaxed = true)
        postRepositoryHandler = PostRepositoryHandler(postRepository, boardRepositoryHandler)

        post = Post(
            id = 1L,
            title = "title",
            content = "content",
            boardId = 1L,
            categoryId = 1L,
        )
    }

    @Test
    fun `게시글을 저장할 수 있다`() {
        // given

        every { postRepository.existsByTitle(any()) } returns false
        every { postRepository.save(any()) } returns post

        // when
        val savedPost = postRepositoryHandler.save(post)

        // then
        assertThat(savedPost).isEqualTo(post)
    }

    @Test
    fun `게시글을 수정할 수 있다`() {
        // given
        val handlerDto = PostUpdateHandlerDto(
            title = "modify title",
            content = "modify content",
            boardId = 1L,
            memberId = 1L,
            postId = 1L
        )

        every { boardRepositoryHandler.validationExistBoardByIdAndMemberId(any(), any()) } returns true
        every { postRepository.findByIdAndBoardId(any(), any()) } returns post

        // when
        val updatedPost = postRepositoryHandler.update(handlerDto)

        // then
        assertAll(
            { assertThat(updatedPost.title).isEqualTo(handlerDto.title) },
            { assertThat(updatedPost.content).isEqualTo(handlerDto.content) }
        )
    }

    @Test
    fun `게시글을 수정할 때 게시글이 없으면 예외가 발생한다`() {
        // given
        val handlerDto = PostUpdateHandlerDto(
            title = "modify title",
            content = "modify content",
            boardId = 1L,
            memberId = 1L,
            postId = 1L
        )

        every { boardRepositoryHandler.validationExistBoardByIdAndMemberId(any(), any()) } returns false
        every { postRepository.findByIdAndBoardId(any(), any()) } returns post

        // when
        val exception = catchThrowable { postRepositoryHandler.update(handlerDto) }

        // then
        assertThat(exception).isInstanceOf(PostException.PostNotFound::class.java)
    }

    @Test
    fun `임시저장된 게시글을 발행한다`() {
        // given
        val postId = 1L

        every { boardRepositoryHandler.validationExistBoardByIdAndMemberId(any(), any()) } returns true
        every { postRepository.findByIdAndBoardId(any(), any()) } returns post

        // when
        val publishedPost = postRepositoryHandler.publish(postId, 1L, 1L)

        // then
        assertThat(publishedPost.postSaveStatus).isEqualTo(PostSaveStatus.PUBLISH)
    }

    @Test
    fun `게시글을 삭제한다`() {
        // given
        val requestDto = PostDeleteRequestHandlerDto(
            postId = 1L,
            boardId = 1L,
            memberId = 1L
        )

        every { boardRepositoryHandler.validationExistBoardByIdAndMemberId(any(), any()) } returns true
        every { postRepository.findByIdAndBoardId(any(), any()) } returns post

        // when
        postRepositoryHandler.delete(requestDto)

        // then
        verify(exactly = 1) { postRepository.findByIdAndBoardId(any(), any()) }
    }

    @Test
    fun `게시글에 상세한 정보를 불러온다`() {
        // given
        val postId = 1L
        val memberId = 1L

        val responseDto = PostDetailResponseDto(
            postId = 1L,
            boardId = 1L,
            memberId = 1L,
            title = "title",
            content = "content",
            categoryName = "categoryName",
            boardName = "boardName",
            memberName = "memberName",
            createdDate = LocalDateTime.now(),
            updatedDate = LocalDateTime.now()
        )

        every { postRepository.getDetailByIdAndMemberId(any(), any()) } returns responseDto

        // when
        val post = postRepositoryHandler.findById(postId, memberId)

        // then
        assertAll(
            { assertThat(post.title).isEqualTo("title") },
            { assertThat(post.content).isEqualTo("content") }
        )
    }

    @Test
    fun `게시글의 간략한 정보를 보여주는 목록을 보여준다`() {
        // given
        val requestDto = PostSummaryRequestDto(
            boardId = 1L,
            categoryId = 1L,
            memberId = 1L,
            page = 1,
            size = 10
        )

        val responseDto = PostSummaryGetResponseDto(
            id = 1L,
            boardId = 1L,
            title = "title",
            content = "content",
            categoryName = "categoryName",
            createdDate = LocalDateTime.now().toString(),
            lastModifiedDate = LocalDateTime.now().toString(),
            categoryId = 1L,
            memberId = 1L,
            viewCount = 1L,
            likeCount = 1L,
            replyCount = 1L,
            postSaveStatus = PostSaveStatus.PUBLISH.name
        )

        every { postRepository.findAllBy(any()) } returns PageImpl(listOf(responseDto))

        // when
        val posts = postRepositoryHandler.findSummaryPosts(requestDto)

        // then
        posts.content.forEach {
            assertAll(
                { assertThat(it.title).isNotNull() },
                { assertThat(it.content).isNotNull() }
            )
        }
    }

    @Test
    fun `게시글 제목이 이미 존재하면 예외가 발생한다`() {
        // given
        val post = Post(
            id = 1L,
            title = "title",
            content = "content",
            boardId = 1L,
            categoryId = 1L,
        )

        every { postRepository.existsByTitle(any()) } returns true

        // when
        val exception = catchThrowable { postRepositoryHandler.save(post) }

        // then
        assertThat(exception).isInstanceOf(PostException.PostTitleAlreadyExist::class.java)
    }
}