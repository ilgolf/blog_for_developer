package me.golf.blog.product.post.exception

import me.golf.blog.global.exception.error.BusinessException
import me.golf.blog.global.exception.error.ErrorCode

sealed class PostException {

    class PostTitleAlreadyExist : BusinessException(ErrorCode.POST_TITLE_ALREADY_EXIST)
    class PostNotFound : BusinessException(ErrorCode.POST_NOT_FOUND)
}
