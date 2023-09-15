package com.gateway.api.exception.exception

import org.springframework.http.HttpStatus

enum class GatewayExceptionCode(
    val code: String,
    val httpStatus: HttpStatus,
    val message: String,
) {
    BAD_REQUEST("400", HttpStatus.BAD_REQUEST, "잘못된 요청이에요."),
    INVALID_REQUEST("403", HttpStatus.UNAUTHORIZED, "잘못된 인증 정보에요."),
    INVALID_TOKEN_FORMAT_REQUEST("403", HttpStatus.UNAUTHORIZED, "인증 형식이 잘못되었습니다."),
    SERVER_ERROR("505", HttpStatus.INTERNAL_SERVER_ERROR, "서버의 오류가 발생했어요. 잠시 후에 요청 부탁드려요!"),
}