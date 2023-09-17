package com.gateway.api.config

import com.gateway.api.util.HeaderProcessor
import com.gateway.api.util.HeaderType
import com.gateway.api.util.TokenParser
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class AuthorizationProdConfig : AuthorizationConfig {

    override fun validateAuthorizationHeaderAndGetAccessToken(request: ServerHttpRequest): String {
        return HeaderProcessor.validateHeaderAndGetAccessToken(request)
    }

    override fun validateAuthorizationHeaderAndGetRefreshToken(request: ServerHttpRequest): String {
        return HeaderProcessor.validateHeaderAndGetRefreshToken(request)
    }

    override fun parserUserId(token: String, headerType: HeaderType): String {
        return TokenParser.parseUserIdFromToken(token, headerType)
    }

}