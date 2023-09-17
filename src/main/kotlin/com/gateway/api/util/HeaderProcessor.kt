package com.gateway.api.util

import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import org.springframework.http.server.reactive.ServerHttpRequest

object HeaderProcessor {

    fun validateHeaderAndGetAccessToken(request: ServerHttpRequest): String {
        validateAuthorizationHeader(request)
        return getTokenFromAuthorizationHeader(request)
    }

    fun validateHeaderAndGetRefreshToken(request: ServerHttpRequest): String {
        validateRefreshTokenHeader(request)
        return getTokenFromAuthorizationHeader(request)
    }

    private fun validateAuthorizationHeader(request: ServerHttpRequest) {
        request.headers[HeaderType.AUTHORIZATION_HEADER.type]?.get(0)
            ?.let { require(it.isNotEmpty()) { throw GatewayException(GatewayExceptionCode.INVALID_REQUEST) } }
            ?: throw GatewayException(GatewayExceptionCode.INVALID_REQUEST)
    }

    private fun validateRefreshTokenHeader(request: ServerHttpRequest) {
        request.headers[HeaderType.REFRESHTOKEN_HEADER.type]?.get(0)
            ?.let { require(it.isNotEmpty()) { throw GatewayException(GatewayExceptionCode.INVALID_REQUEST) } }
            ?: throw GatewayException(GatewayExceptionCode.INVALID_REQUEST)
    }

    private fun getTokenFromAuthorizationHeader(request: ServerHttpRequest): String {
        return request.headers[HeaderType.AUTHORIZATION_HEADER.type]!![0]
    }
}