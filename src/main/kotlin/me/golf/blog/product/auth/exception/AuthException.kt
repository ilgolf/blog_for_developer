package me.golf.blog.product.auth.exception

import me.golf.blog.global.exception.error.BusinessException
import me.golf.blog.global.exception.error.ErrorCode

sealed class AuthException {
    class RefreshInvalidException : BusinessException(ErrorCode.REFRESH_TOKEN_INVALID)
    class AuthorizationFailException(email: String) : BusinessException(ErrorCode.AUTHORIZATION_FAIL, email)
}
