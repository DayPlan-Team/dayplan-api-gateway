package com.gateway.api.advice

import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import com.gateway.api.util.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class ExceptionController {

    @ResponseStatus
    @ExceptionHandler(GatewayException::class)
    fun applyGatewayException(e: GatewayException): Mono<ResponseEntity<ExceptionResponse>> {
        val httpStatus = HttpStatus.valueOf(e.status)
        log.info("Gateway Exception http status = $httpStatus")
        return Mono.just(
            ResponseEntity(
                ExceptionResponse(
                    status = e.status,
                    code = e.errorCode,
                    message = e.message
                ),
                httpStatus,
            )
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    fun applyBadRequestHandler(e: IllegalArgumentException): Mono<ResponseEntity<ExceptionResponse>> {

        return Mono.just(
            ResponseEntity(
                ExceptionResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    code = GatewayExceptionCode.BAD_REQUEST.errorCode,
                    message = GatewayExceptionCode.BAD_REQUEST.message,
                ),
                HttpStatus.BAD_REQUEST,
            )
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    fun applyServerErrorHandler(e: Exception): Mono<ResponseEntity<ExceptionResponse>> {
        return Mono.just(
            ResponseEntity(
                ExceptionResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    code = GatewayExceptionCode.SERVER_ERROR.errorCode,
                    message = GatewayExceptionCode.SERVER_ERROR.message,
                ),
                HttpStatus.INTERNAL_SERVER_ERROR,
            )
        )
    }

    companion object : Logger()
}