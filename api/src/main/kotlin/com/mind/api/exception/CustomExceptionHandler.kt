package com.mind.api.exception

import com.mind.api.common.dto.ResponseException
import com.mind.core.enums.ResponseEnums
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ResponseException<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }
        return ResponseEntity(ResponseException(ResponseEnums.BAD.code, ResponseEnums.BAD.message, errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception): ResponseEntity<ResponseException<Map<String, String>>> {
        val errors = mapOf("cause" to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(ResponseException(ResponseEnums.ERROR.code, ResponseEnums.ERROR.message, errors), HttpStatus.BAD_REQUEST)
    }

}
