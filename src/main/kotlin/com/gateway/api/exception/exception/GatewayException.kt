package com.gateway.api.exception.exception

class GatewayException(
    private val code: GatewayExceptionCode,
) : RuntimeException() {

    override val cause: Throwable
        get() = Throwable(code.errorCode)
    override val message: String
        get() = code.message

    val status: Int
        get() = code.status

    val errorCode: String
        get() = code.errorCode
}