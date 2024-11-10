package com.mind.api.common.dto

import io.swagger.v3.oas.annotations.media.Schema

abstract class PagingRequestData {
    @Schema(name = "페이지 번호")
    var page: Int = 0
    @Schema(name = "페이지 사이즈")
    var size: Int = 0
}

class PagingResponseData(
    @Schema(name = "전체 데이터 수")
    val total: Int,
    @Schema(name = "페이지 번호")
    val page: Int,
    @Schema(name = "페이지 사이즈")
    val size: Int
)

class PagingWrapper<T>(
    @Schema(name = "페이징")
    val paging: PagingResponseData,
    @Schema(name = "데이터 목록")
    val list: T?
)
