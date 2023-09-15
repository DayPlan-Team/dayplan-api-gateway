package com.gateway.api.config

import com.gateway.api.util.HeaderProcessor
import com.gateway.api.util.TokenParser
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class AuthorizationProdConfig : AuthorizationConfig {

    override fun validateAuthorizationHeaderAndGetAccessToken(request: ServerHttpRequest): String {
        return HeaderProcessor.validateHeaderAndGetToken(request)
    }

    override fun parserUserId(token: String): String {
        return TokenParser.parseUserIdFromToken(token)
    }

}