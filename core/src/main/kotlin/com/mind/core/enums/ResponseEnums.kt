package com.mind.core.enums

enum class ResponseEnums(val code: String, val message: String) {
    // default
    SUCCESS("200", "정상 처리 되었습니다."),
    FAIL("400", "잘못된 요청 입니다."),
    ERROR("500", "오류가 발생 했습니다."),

    // member [1000]
    MEMBER_SIGNUP_FAIL("1000", "회원가입 처리 중 오류가 발생했습니다."),
    MEMBER_EMAIL_DUPLICATED("1001", "중복된 이메일이 존재합니다."),

    // authorization [2000]
    AUTHENTICATION_FAIL("2000", "로그인 실패했습니다."),
    AUTHENTICATION_INCORRECT("2001", "아이디 비밀번호를 확인해주세요."),
}
