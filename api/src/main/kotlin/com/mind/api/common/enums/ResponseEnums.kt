package com.mind.api.common.enums

enum class ResponseEnums(val code: String, val message: String) {
    // default
    SUCCESS("200", "정상 처리 되었습니다"),
    FAIL("400", "잘못된 요청 입니다."),
    ERROR("500", "오류가 발생 했습니다.")
}
