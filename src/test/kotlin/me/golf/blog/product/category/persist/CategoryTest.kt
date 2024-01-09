package me.golf.blog.product.category.persist

import me.golf.blog.product.category.util.GivenCategory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@Suppress("NonAsciiCharacters")
class CategoryTest {

    private lateinit var category: Category

    @BeforeEach
    fun setUp() {
        category = GivenCategory.getParentCategory()
    }

    @Test
    fun `카테고리를 수정할 수 있다`() {
        // given
        val name = "modify name"
        val order = 2

        // when
        category.modify(name, order)

        // then
        assertAll(
            { assertThat(category.name).isEqualTo(name) },
            { assertThat(category.order).isEqualTo(order) }
        )
    }

    @Test
    fun `카테고리를 사용할 수 있다`() {
        // given

        // when
        category.use()

        // then
        assertThat(category.isUsed).isTrue
    }

    @Test
    fun `카테고리를 사용하지 않을 수 있다`() {
        // given

        // when
        category.unUse()

        // then
        assertThat(category.isUsed).isFalse
    }

    @Test
    fun `카테고리의 부모를 변경할 수 있다`() {
        // given
        val givenCategory = GivenCategory.getCategory()

        givenCategory.parentId = 3L

        // when
        category.changeParent(1L)

        // then
        assertThat(category.parentId).isEqualTo(1L)
    }
}