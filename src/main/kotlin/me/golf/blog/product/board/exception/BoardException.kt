package me.golf.blog.product.board.exception

import me.golf.blog.global.exception.error.BusinessException
import me.golf.blog.global.exception.error.ErrorCode

sealed class BoardException {
    class AlreadyExistException(value: String) : BusinessException(ErrorCode.BOARD_ALREADY_EXIST, value)
}
