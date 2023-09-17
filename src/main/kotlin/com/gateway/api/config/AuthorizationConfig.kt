package com.gateway.api.config

import com.gateway.api.util.HeaderType
import org.springframework.http.server.reactive.ServerHttpRequest

interface AuthorizationConfig {

    fun validateAuthorizationHeaderAndGetAccessToken(request: ServerHttpRequest): String

    fun validateAuthorizationHeaderAndGetRefreshToken(request: ServerHttpRequest): String

    fun parserUserId(token: String, headerType: HeaderType): String

}