package com.mind.api.common.dto

import com.mind.core.enums.ResponseEnums
import org.springframework.http.ResponseEntity

object ResponseData {
    fun <T> success(response: T): ResponseEntity<ResponseSuccess<T>> = ResponseEntity.ok(ResponseSuccess(ResponseEnums.SUCCESS.code, ResponseEnums.SUCCESS.message, response))
    fun fail(responseEnums: ResponseEnums) = ResponseEntity.badRequest().body(ResponseFail(responseEnums.code, responseEnums.message))
}

class ResponseSuccess<T>(val code: String, val message: String, val data: T)

class ResponseFail(val code: String, val message: String)

class ResponseException<T> (val code: String = ResponseEnums.ERROR.code, val message: String = ResponseEnums.ERROR.message, val error: T? = null)
