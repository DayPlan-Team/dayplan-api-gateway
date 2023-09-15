package com.gateway.api.config

import org.springframework.http.server.reactive.ServerHttpRequest

interface AuthorizationConfig {

    fun validateAuthorizationHeaderAndGetAccessToken(request: ServerHttpRequest): String

    fun parserUserId(token: String): String

}