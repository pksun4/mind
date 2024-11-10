package com.mind.api.common.dto

abstract class PagingRequestData {
    var page: Int = 0
    var size: Int = 0
}

class PagingResponseData(
    val total: Int,
    val page: Int,
    val size: Int
)

class PagingWrapper<T>(
    val paging: PagingResponseData,
    val list: T?
)
