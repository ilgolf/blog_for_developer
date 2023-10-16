package me.golf.blog.product.board.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.blog.commonutils.IntegrationTest
import me.golf.blog.global.jwt.TokenProvider
import me.golf.blog.global.security.CustomUserDetailsService
import me.golf.blog.product.board.dto.BoardCreatePostRequestDto
import me.golf.blog.product.board.dto.BoardUpdatePutRequestDto
import me.golf.blog.product.board.persist.Board
import me.golf.blog.product.board.persist.BoardRepository
import me.golf.blog.product.board.util.GivenBoard
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberRepository
import me.golf.blog.product.member.util.GivenMember
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
class BoardIntegrationTest

@Autowired
constructor(
    private val memberRepository: MemberRepository,
    private val boardRepository: BoardRepository,
    private val objectMapper: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
): IntegrationTest() {

    private lateinit var member: Member
    private lateinit var board: Board
    private var token: String = ""

    private val simpleBoardResponse = responseFields(
        fieldWithPath("boardId").description("게시판 식별자"),
        fieldWithPath("boardUrl").description("게시판 url")
    )

    @BeforeEach
    fun setUp() {
        member = memberRepository.save(GivenMember.toMember())
        board = boardRepository.save(GivenBoard.createBoardEntity(member.id))

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        token = tokenProvider.createToken(member.id, authToken).accessToken
    }

    @Test
    fun `게시판 저장 통합 테스트`() {
        val requestDto = BoardCreatePostRequestDto(
            title = "testTitle-blog",
            description = "안녕하세요.",
            boardUrl = "https://golf.dev-stroy.com"
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(post("/boards")
            .header("Authorization", "Bearer $token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("board/request/create", requestFields(
                fieldWithPath("title").description("게시판 제목"),
                fieldWithPath("description").description("자기 소개 글"),
                fieldWithPath("boardUrl").description("게시판 url")
            )))
            .andDo(document("board/response/create", simpleBoardResponse))
    }

    @Test
    fun `게시판 저장 시 title, boardUrl 이 빈 값이면 예외`() {
        val requestDto = BoardCreatePostRequestDto(
            title = "",
            description = "",
            boardUrl = ""
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(post("/boards")
            .header("Authorization", "Bearer $token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `게시판 저장 시 title, boardUrl 이 최대 길이를 넘어가면 예외`() {
        val requestDto = BoardCreatePostRequestDto(
            title = "newTitlenewTitlenewTitlenewTitlenewTitlenewTitlenewTitlenewTitlenewTitlenewTitlenewTitlenew",
            description = "",
            boardUrl = "https://www.golf.co.kr/image/3, https://www.golf.co.kr/image/3, https://www.golf.co.kr/image/3"
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(post("/boards")
            .header("Authorization", "Bearer $token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `게시판 수정 통합 테스트`() {
        val requestDto = BoardUpdatePutRequestDto(
            title = "new-title",
            description = "안녕하세요. 반갑습니다."
        )

        val body = objectMapper.writeValueAsString(requestDto)

        mockMvc.perform(put("/boards/${board.id}")
            .header("Authorization", "Bearer $token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("board/request/update", requestFields(
                fieldWithPath("title").description("게시판 제목"),
                fieldWithPath("description").description("자기 소개 글")
            )))
            .andDo(document("board/response/update", simpleBoardResponse))
    }

    @Test
    fun `게시판 요약 조회 통합 테스트`() {
        mockMvc.perform(get("/boards")
            .header("Authorization", "Bearer $token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("board/request/summary"))
            .andDo(document("board/response/summary", responseFields(
                fieldWithPath("[].boardId").description("게시판 식별자"),
                fieldWithPath("[].title").description("게시판 제목"),
                fieldWithPath("[].boardUrl").description("게시판 url")
            )))
    }

    @Test
    fun `게시판 상세 조회 통합 테스트`() {
        mockMvc.perform(get("/boards/${board.id}")
            .header("Authorization", "Bearer $token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(document("board/request/detail"))
            .andDo(document("board/response/detail", responseFields(
                fieldWithPath("boardId").description("게시판 식별자"),
                fieldWithPath("title").description("게시판 제목"),
                fieldWithPath("description").description("게시판 소개"),
                fieldWithPath("boardUrl").description("게시판 url")
            )))
    }
}