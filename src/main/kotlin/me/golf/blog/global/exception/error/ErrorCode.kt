package me.golf.blog.global.exception.error

enum class ErrorCode(val message: String, val status: Int) {

    // common
    INVALID_INPUT_VALUE("올바르지 않은 값입니다.", 400),
    METHOD_NOT_ALLOWED("올바르지 않은 요청 메서드입니다.", 405),
    INTERNAL_SERVER_ERROR("치명적인 서버 오류입니다.", 500),
    HANDLE_ACCESS_DENIED("해당권한으로는 접근할 수 없습니다", 403),
    INVALID_TYPE_VALUE("해당 값은 들어올 수 없습니다. 값을 확인해주세요", 400),

    // member
    MEMBER_NOT_FOUND("회원을 찾지 못하였습니다.", 400),
    PASSWORD_CONFIRM_MISS_MATCH("비밀번호와 비밀번호 확인이 일치하지 않습니다.", 400),
    MEMBER_ALREADY_EXIST("이미 존재하는 회원입니다.", 400),

    // Authentication
    REFRESH_TOKEN_INVALID("리프레시 토큰 인증에 실패했습니다.", 401),
    AUTHORIZATION_FAIL("인증에 실패했습니다.", 401),

    // BOARD
    BOARD_ALREADY_EXIST("해당 내용의 게시판이 이미 존재합니다.", 400),
    BOARD_NOT_FOUND("게시판이 존재하지 않습니다.", 400),
    BOARD_EXCEED_REACH("게시판 보유 횟수인 4개를 초과했습니다", 400),
    NON_EXISTENT_MEMBER_BOARD_CREATION_ERROR("존재하지 않는 회원에 대해서 게시판 생성할 수 없습니다.", 400);
}
