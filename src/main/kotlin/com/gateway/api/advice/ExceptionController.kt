package com.gateway.api.advice

import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice(annotations = [RestController::class])
class ExceptionController {

    @ResponseStatus
    @ExceptionHandler
    fun applyUnAuthorizedHandler(e: GatewayException): Mono<ResponseEntity<ErrorResponse>> {
        return Mono.just(
            ResponseEntity(
                ErrorResponse(e.code, e.message),
                e.status,
            )
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    fun applyBadRequestHandler(e: IllegalArgumentException): Mono<ResponseEntity<ErrorResponse>> {
        return Mono.just(
            ResponseEntity(
                ErrorResponse(GatewayExceptionCode.BAD_REQUEST.code, GatewayExceptionCode.BAD_REQUEST.message),
                HttpStatus.BAD_REQUEST,
            )
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    fun applyServerErrorHandler(e: Exception): Mono<ResponseEntity<ErrorResponse>> {
        return Mono.just(
            ResponseEntity(
                ErrorResponse(GatewayExceptionCode.SERVER_ERROR.code, GatewayExceptionCode.SERVER_ERROR.message),
                HttpStatus.BAD_REQUEST,
            )
        )
    }
}