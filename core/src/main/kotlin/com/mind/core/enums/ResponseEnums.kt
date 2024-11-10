package com.mind.core.enums

enum class ResponseEnums(val code: String, val message: String) {
    // default
    SUCCESS("200", "정상 처리 되었습니다."),
    BAD("400", "잘못된 요청입니다."),
    ERROR("500", "오류가 발생 했습니다."),

    // member [1000]
    MEMBER_SIGNUP_FAIL("1000", "회원가입 처리 중 오류가 발생했습니다."),
    MEMBER_EMAIL_DUPLICATED("1001", "중복된 이메일이 존재합니다."),
    MEMBER_NONE("1002", "회원정보를 찾을 수 없습니다."),
    MEMBER_NONE_TOKEN("1003", "토큰 정보가 없습니다."),
    MEMBER_PASSWORD_INCORRECT("1004", "기존 패스워드가 일치하지 않습니다."),

    // authorization [2000]
    AUTHENTICATION_FAIL("2000", "로그인 실패했습니다."),
    AUTHENTICATION_INCORRECT("2001", "아이디 비밀번호를 확인해주세요."),

    // board [3000]
    BOARD_NONE("3000", "게시글이 존재하지 않습니다."),
    BOARD_NONE_AUTH("3001", "게시글을 수정할 권한이 없습니다.")
}
