package com.gateway.api.filter

import com.fasterxml.jackson.core.JsonProcessingException
import com.gateway.api.config.AuthorizationConfig
import com.gateway.api.util.HeaderType
import com.gateway.api.util.TokenParser
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class RefreshTokenHeaderFilter(
    private val antPathMatcher: AntPathMatcher,
) : AbstractGatewayFilterFactory<AuthorizationConfig>() {

    companion object {
        private val WHITE_LIST = arrayOf(
            "/user/reissue/accesstoken"
        )
    }

    override fun apply(config: AuthorizationConfig): GatewayFilter = GatewayFilter { exchange, chain ->
        val request = exchange.request
        if (!isWhiteList(request.uri.path)) return@GatewayFilter chain.filter(exchange)

        try {
            val accessToken = config.validateAuthorizationHeaderAndGetRefreshToken(request)
            val userId = TokenParser.parseUserIdFromToken(accessToken, HeaderType.REFRESHTOKEN_HEADER)

            val modifiedRequest = exchange.request.mutate().header("UserId", userId).build()
            return@GatewayFilter chain.filter(exchange.mutate().request(modifiedRequest).build())

        } catch (e: JsonProcessingException) {
            return@GatewayFilter onError(exchange)
        }
    }

    private fun isWhiteList(requestURI: String): Boolean {
        return WHITE_LIST.any { antPathMatcher.match(it, requestURI) }
    }

    private fun onError(exchange: ServerWebExchange): Mono<Void> {
        val response = exchange.response
        response.setStatusCode(HttpStatus.UNAUTHORIZED)
        return response.setComplete()
    }
}