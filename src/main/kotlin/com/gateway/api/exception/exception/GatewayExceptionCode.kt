package com.gateway.api.exception.exception

import org.springframework.http.HttpStatus

enum class GatewayExceptionCode(
    val code: String,
    val errorCode: String,
    val httpStatus: HttpStatus,
    val message: String,
) {
    BAD_REQUEST("400", "APGW0001", HttpStatus.BAD_REQUEST, "잘못된 요청이에요."),
    INVALID_REQUEST("403", "APGW0002", HttpStatus.UNAUTHORIZED, "잘못된 인증 정보에요."),
    INVALID_TOKEN_FORMAT_REQUEST("403", "APGW0003", HttpStatus.UNAUTHORIZED, "인증 형식이 잘못되었어요."),
    ACCESSTOKEN_EXPIRED("401", "APGW0004", HttpStatus.UNAUTHORIZED, "엑세스 토큰이 만료 되었어요."),
    REFRESHTOKEN_EXPIRED("401", "APGW0005", HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료 되었어요. 다시 로그인 해주세요."),

    // 서버 에러
    SERVER_ERROR("505", "APGW1001", HttpStatus.INTERNAL_SERVER_ERROR, "서버의 오류가 발생했어요. 잠시 후에 요청 부탁드려요."),

}