package com.gateway.api.exception.exception

import org.springframework.http.HttpStatus

class GatewayException(
    private val errorCode: GatewayExceptionCode,
) : RuntimeException() {

    override val cause: Throwable
        get() = Throwable(errorCode.code)

    override val message: String
        get() = errorCode.message

    val code: String
        get() = errorCode.code

    val status: HttpStatus
        get() = errorCode.httpStatus
}