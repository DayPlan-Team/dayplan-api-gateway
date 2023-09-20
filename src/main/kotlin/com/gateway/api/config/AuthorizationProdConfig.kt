package com.gateway.api.config

import com.gateway.api.util.HeaderProcessor
import com.gateway.api.util.HeaderType
import com.gateway.api.util.Logger
import com.gateway.api.util.TokenParser
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class AuthorizationProdConfig(
    private val tokenParser: TokenParser,
) : AuthorizationConfig {

    override fun validateAuthorizationHeaderAndGetAccessToken(request: ServerHttpRequest): String {
        log.info("access token request")
        log.info("uri = ${request.uri}")
        return HeaderProcessor.validateHeaderAndGetAccessToken(request)
    }

    override fun validateAuthorizationHeaderAndGetRefreshToken(request: ServerHttpRequest): String {
        log.info("refresh token request")
        log.info("uri = ${request.uri}")
        return HeaderProcessor.validateHeaderAndGetRefreshToken(request)
    }

    override fun parserUserId(token: String, headerType: HeaderType): String {
        return tokenParser.parseUserIdFromToken(token, headerType)
    }

    companion object : Logger()

}