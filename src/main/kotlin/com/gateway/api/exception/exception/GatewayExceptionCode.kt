package com.gateway.api.exception.exception

enum class GatewayExceptionCode(
    val status: Int,
    val errorCode: String,
    val message: String,
) {
    BAD_REQUEST(400, "APGW0001", "잘못된 요청이에요."),
    INVALID_REQUEST(405, "APGW0002", "잘못된 인증 정보에요."),
    INVALID_TOKEN_FORMAT_REQUEST(403, "APGW0003", "인증 형식이 잘못되었어요."),
    ACCESSTOKEN_EXPIRED(403, "APGW0004", "엑세스 토큰이 만료 되었어요."),
    REFRESHTOKEN_EXPIRED(403, "APGW0005", "리프레시 토큰이 만료 되었어요. 다시 로그인 해주세요."),

    // 서버 에러
    SERVER_ERROR(500, "APGW1001", "서버의 오류가 발생했어요. 잠시 후에 요청 부탁드려요."),

}