package com.gateway.api.filter

import com.fasterxml.jackson.core.JsonProcessingException
import com.gateway.api.config.AuthorizationConfig
import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.util.HeaderType
import com.gateway.api.util.Logger
import com.gateway.api.util.TokenParser
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher

@Component
class AuthorizationHeaderFilter(
    private val antPathMatcher: AntPathMatcher,
    private val tokenParser: TokenParser,
    private val errorFilter: ErrorFilter,
) : AbstractGatewayFilterFactory<AuthorizationConfig>() {

    companion object : Logger() {
        private val WHITE_LIST = arrayOf(
            "/",
            "/static/**",
            "/favicon.ico",
            "/user/login/**",
            "/user/logout/**",
            "/user/authentication/reissue/accesstoken"
        )
    }

    override fun apply(config: AuthorizationConfig): GatewayFilter = GatewayFilter { exchange, chain ->
        val request = exchange.request
        log.info("header request = ${request.uri}")

        if (isWhiteList(request.uri.path)) return@GatewayFilter chain.filter(exchange)

        try {
            val accessToken = config.validateAuthorizationHeaderAndGetAccessToken(request)
            val userId = tokenParser.parseUserIdFromToken(accessToken, HeaderType.AUTHORIZATION_HEADER)

            val modifiedRequest = exchange.request.mutate().header("UserId", userId).build()
            return@GatewayFilter chain.filter(exchange.mutate().request(modifiedRequest).build())

        } catch (e: JsonProcessingException) {
            return@GatewayFilter errorFilter.onError(exchange, e)
        } catch (e: GatewayException) {
            return@GatewayFilter errorFilter.onError(exchange, e)
        }
    }

    private fun isWhiteList(requestURI: String): Boolean {
        return WHITE_LIST.any { antPathMatcher.match(it, requestURI) }
    }
}