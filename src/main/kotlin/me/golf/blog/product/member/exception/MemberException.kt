package me.golf.blog.product.member.exception

import me.golf.blog.global.exception.error.BusinessException
import me.golf.blog.global.exception.error.ErrorCode

sealed class MemberException {
    class MemberNotFoundException(value: String) : BusinessException(ErrorCode.MEMBER_NOT_FOUND, value)
    class PasswordConfirmMissMatchException : BusinessException(ErrorCode.PASSWORD_CONFIRM_MISS_MATCH)
    class AlreadyExistException(value: String) : BusinessException(ErrorCode.MEMBER_ALREADY_EXIST, value)
}
