package me.golf.blog.product.post.persist.repository

import me.golf.blog.commonutils.IntegrationTest
import me.golf.blog.product.board.persist.Board
import me.golf.blog.product.board.persist.BoardRepository
import me.golf.blog.product.board.util.GivenBoard
import me.golf.blog.product.category.persist.Category
import me.golf.blog.product.category.persist.CategoryRepository
import me.golf.blog.product.category.util.GivenCategory
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.repository.MemberRepository
import me.golf.blog.product.member.util.GivenMember
import me.golf.blog.product.post.dto.PostSummaryRequestDto
import me.golf.blog.product.post.persist.Post
import me.golf.blog.product.post.util.GivenPost
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
class PostCustomRepositoryImplTest
@Autowired constructor(
    private val sut: PostRepository,
    private val memberRepository: MemberRepository,
    private val boardRepository: BoardRepository,
    private val categoryRepository: CategoryRepository
): IntegrationTest() {

    private lateinit var post: Post
    private lateinit var member: Member
    private lateinit var board: Board
    private  lateinit var category: Category

    @BeforeEach
    fun setUp() {

        member = memberRepository.save(GivenMember.toMember())
        board = boardRepository.save(GivenBoard.createBoardEntity(member.id))
        category = categoryRepository.save(GivenCategory.getCategory())
        post = sut.save(GivenPost.createPublishedPost(board.id, category.id))
    }

    @Test
    @DisplayName("게시글 상세 조회")
    fun getDetailByIdAndMemberId() {
        // given

        // when
        val result = sut.getDetailByIdAndMemberId(post.id, member.id)

        // then
        assertThat(result).isNotNull
    }

    @Test
    @DisplayName("게시글 목록 조회")
    fun findAllBy() {
        // given
        val requestDto = PostSummaryRequestDto(
            boardId = board.id,
            categoryId = category.id,
            memberId = member.id,
            page = 0,
            size = 10,
        )

        // when
        val result = sut.findAllBy(requestDto)

        // then
        assertThat(result.content).isNotEmpty
    }
}