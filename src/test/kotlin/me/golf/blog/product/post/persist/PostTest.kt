package me.golf.blog.product.post.persist

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters")
class PostTest {

    private lateinit var post: Post

    @BeforeEach
    fun setUp() {
        post = Post(
            title = "title",
            content = "content",
            boardId = 1L,
            categoryId = 1L
        )
    }

    @Test
    fun `게시판을 수정한다`() {
        // given
        val title = "update title"
        val content = "update content"

        // when
        post.modify(title, content)

        // then
        assertSoftly {
            it.assertThat(post.title).isEqualTo(title)
            it.assertThat(post.content).isEqualTo(content)
        }
    }

    @Test
    fun `게시판을 삭제한다`() {
        // given
        val deleted = true

        // when
        post.delete()

        // then
        assertThat(post.deleted).isEqualTo(deleted)
    }

    @Test
    fun `게시판을 게시한다`() {

        // when
        post.publish()

        // then
        assertThat(post.postSaveStatus).isEqualTo(PostSaveStatus.PUBLISH)
    }

    @Test
    fun `게시판이 최초 생성되면 metrics 값이 0이다`() {

        // when
        val postMetrics = post.postMetrics

        // then
        assertSoftly {
            it.assertThat(postMetrics.likeCount).isEqualTo(0)
            it.assertThat(postMetrics.replyCount).isEqualTo(0)
            it.assertThat(postMetrics.viewCount).isEqualTo(0)
        }
    }
}