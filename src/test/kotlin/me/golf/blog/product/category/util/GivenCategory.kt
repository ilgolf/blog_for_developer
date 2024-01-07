package me.golf.blog.product.category.util

import me.golf.blog.product.category.persist.Category

object GivenCategory {

    private const val CATEGORY_NAME = "categoryName"
    private const val CATEGORY_ID = 1L
    private const val CATEGORY_PARENT_ID = 2L
    private const val CATEGORY_SORT = 1
    private const val CATEGORY_USE_YN = true

    fun getCategory() = Category(
        id = CATEGORY_ID,
        name = CATEGORY_NAME,
        parentId = CATEGORY_PARENT_ID,
        order = CATEGORY_SORT,
        isUsed = CATEGORY_USE_YN
    )

    fun getCategory(parentId: Long) =
        Category(
            name = CATEGORY_NAME,
            parentId = parentId,
            order = CATEGORY_SORT,
            isUsed = CATEGORY_USE_YN
        )

    fun getParentCategory() = Category(
        id = CATEGORY_PARENT_ID,
        name = CATEGORY_NAME,
        parentId = null,
        order = CATEGORY_SORT,
        isUsed = CATEGORY_USE_YN
    )
}