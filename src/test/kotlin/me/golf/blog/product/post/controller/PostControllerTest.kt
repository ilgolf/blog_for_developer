package me.golf.blog.product.post.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.blog.commonutils.IntegrationTest
import me.golf.blog.global.jwt.TokenProvider
import me.golf.blog.global.security.CustomUserDetailsService
import me.golf.blog.product.board.persist.Board
import me.golf.blog.product.board.persist.BoardRepository
import me.golf.blog.product.board.util.GivenBoard
import me.golf.blog.product.category.persist.Category
import me.golf.blog.product.category.persist.CategoryRepository
import me.golf.blog.product.category.util.GivenCategory
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberRepository
import me.golf.blog.product.member.util.GivenMember
import me.golf.blog.product.post.dto.PostCreatePostRequestDto
import me.golf.blog.product.post.dto.PostUpdatePutRequestDto
import me.golf.blog.product.post.persist.Post
import me.golf.blog.product.post.persist.repository.PostRepository
import me.golf.blog.product.post.util.GivenPost
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@Suppress("NonAsciiCharacters")
class PostControllerTest
@Autowired
constructor(
    private val memberRepository: MemberRepository,
    private val boardRepository: BoardRepository,
    private val postRepository: PostRepository,
    private val categoryRepository: CategoryRepository,
    private val objectMapper: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : IntegrationTest() {

    private lateinit var member: Member
    private lateinit var board: Board
    private lateinit var tempPost: Post
    private lateinit var post: Post
    private lateinit var category: Category
    private var token: String = ""

    private val simplePostResponse = responseFields(
        fieldWithPath("postId").description("게시글 식별자"),
        fieldWithPath("title").description("게시글 제목"),
    )

    @BeforeEach
    fun setUp() {
        member = memberRepository.save(GivenMember.toMember())
        board = boardRepository.save(GivenBoard.createBoardEntity(member.id))
        category = categoryRepository.save(GivenCategory.getParentCategory())
        tempPost = postRepository.save(GivenPost.createPost(board.id, categoryId = category.id))
        post = postRepository.save(GivenPost.createPublishedPost(board.id, categoryId = category.id))

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        token = tokenProvider.createToken(member.id, authToken).accessToken
    }

    @Test
    fun `포스트 임시 생성`() {
        // given
        val requestDto = PostCreatePostRequestDto(
            title = "포스트 제목",
            content = "포스트 내용",
            categoryId = 1L,
            boardId = board.id
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when
        mockMvc.perform(
            post("/post")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .content(body)
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "post/create", requestFields(
                        fieldWithPath("title").description("포스트 제목"),
                        fieldWithPath("content").description("포스트 내용"),
                        fieldWithPath("categoryId").description("카테고리 식별자"),
                        fieldWithPath("boardId").description("게시판 식별자")
                    )
                )
            )
            .andDo(document("post/create", simplePostResponse))
    }

    @Test
    fun `포스트 임시 생성 시 title, content, categoryId, boardId 가 빈 값이면 예외`() {
        // given
        val requestDto = PostCreatePostRequestDto(
            title = "",
            content = "",
            categoryId = 0L,
            boardId = 0L
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when
        mockMvc.perform(
            post("/post")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .content(body)
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    fun `포스트 임시 생성 시 title, content, categoryId, boardId 가 최대 길이를 넘어가면 예외`() {
        // given
        val requestDto = PostCreatePostRequestDto(
            title = "a".repeat(51),
            content = "a".repeat(1001),
            categoryId = 0L,
            boardId = 0L
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when
        mockMvc.perform(
            post("/post")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .content(body)
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    fun `포스트 임시 생성 시 categoryId, boardId 가 0 이하이면 예외`() {
        // given
        val requestDto = PostCreatePostRequestDto(
            title = "포스트 제목",
            content = "포스트 내용",
            categoryId = -1L,
            boardId = -1L
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when
        mockMvc.perform(
            post("/post")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .content(body)
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    fun `포스트를 공개적으로 발행한다`() {
        // given

        // when, then
        mockMvc.perform(
            patch("/post/${board.id}/${tempPost.id}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(document("post/publish", simplePostResponse))
    }

    @Test
    fun `포스트를 업데이트 한다`() {
        // given
        val requestDto = PostUpdatePutRequestDto(
            title = "수정된 제목",
            content = "수정된 내용",
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when, then
        mockMvc.perform(
            put("/post/${board.id}/${tempPost.id}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .content(body)
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "post/update", requestFields(
                        fieldWithPath("title").description("포스트 제목"),
                        fieldWithPath("content").description("포스트 내용"),
                    )
                )
            )
            .andDo(document("post/update", simplePostResponse))
    }

    @Test
    fun `포스트를 업데이트 할 때 title, content 이 빈 값이면 예외`() {
        // given
        val requestDto = PostUpdatePutRequestDto(
            title = "",
            content = "",
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when, then
        mockMvc.perform(
            put("/post/${board.id}/${tempPost.id}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .content(body)
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    fun `포스트를 삭제할 수 있다`() {
        // given

        // when, then
        mockMvc.perform(
            delete("/post/${board.id}/${tempPost.id}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType("application/json")
        )
            .andExpect(status().isNoContent)
            .andDo(print())
    }

    @Test
    fun `포스트 상세조회가 가능하다`() {
        // given

        // when, then
        mockMvc.perform(
            get("/post/${post.id}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(document("post/detail", responseFields(
                fieldWithPath("postId").description("게시글 식별자"),
                fieldWithPath("boardId").description("게시글 제목"),
                fieldWithPath("memberId").description("게시글 내용"),
                fieldWithPath("title").description("카테고리 식별자"),
                fieldWithPath("content").description("게시판 식별자"),
                fieldWithPath("categoryName").description("좋아요 수"),
                fieldWithPath("boardName").description("댓글 수"),
                fieldWithPath("writer").description("조회 수"),
                fieldWithPath("createdDate").description("게시글 상태"),
                fieldWithPath("updatedDate").description("카테고리 이름"),
            )))
    }

    @Test
    fun `포스트 목록을 조회한다`() {
        // given
        for (i in 1..10) {
            postRepository.save(
                GivenPost.createPost(
                    title = "title$i",
                    content = "content",
                    boardId = board.id,
                    categoryId = 1L
                )
            )
        }

        // when, then
        mockMvc.perform(
            get("/post/${board.id}/${category.id}?page=0&size=10}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType("application/json")
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "post/list", responseFields(
                        fieldWithPath("data[].id").description("게시글 식별자"),
                        fieldWithPath("data[].title").description("게시글 제목"),
                        fieldWithPath("data[].content").description("게시글 내용"),
                        fieldWithPath("data[].categoryId").description("카테고리 식별자"),
                        fieldWithPath("data[].boardId").description("게시판 식별자"),
                        fieldWithPath("data[].likeCount").description("좋아요 수"),
                        fieldWithPath("data[].replyCount").description("댓글 수"),
                        fieldWithPath("data[].viewCount").description("조회 수"),
                        fieldWithPath("data[].postSaveStatus").description("게시글 상태"),
                        fieldWithPath("data[].categoryName").description("카테고리 이름"),
                        fieldWithPath("data[].memberId").description("작성자 식별자"),
                        fieldWithPath("data[].createdDate").description("생성일"),
                        fieldWithPath("data[].lastModifiedDate").description("수정일"),
                        fieldWithPath("number").description("페이지 번호"),
                        fieldWithPath("pageSize").description("페이지 크기"),
                        fieldWithPath("totalPage").description("전체 페이지 수"),
                        fieldWithPath("totalElements").description("전체 요소 개수"),
                    )
                )
            )
    }
}
