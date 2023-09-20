package com.gateway.api.filter

import com.fasterxml.jackson.core.JsonProcessingException
import com.gateway.api.config.AuthorizationConfig
import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import com.gateway.api.util.HeaderType
import com.gateway.api.util.Logger
import com.gateway.api.util.TokenParser
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component

@Component
class RefreshTokenHeaderFilter(
    private val tokenParser: TokenParser,
    private val errorFilter: ErrorFilter,
) : AbstractGatewayFilterFactory<AuthorizationConfig>() {

    override fun apply(config: AuthorizationConfig): GatewayFilter = GatewayFilter { exchange, chain ->
        val request = exchange.request

        log.info("request Header = ${request.headers}")

        try {
            val refreshToken = config.validateAuthorizationHeaderAndGetRefreshToken(request)
            val userId = tokenParser.parseUserIdFromToken(refreshToken, HeaderType.REFRESHTOKEN_HEADER)

            val modifiedRequest = exchange.request.mutate().header("UserId", userId).build()
            return@GatewayFilter chain.filter(exchange.mutate().request(modifiedRequest).build())

        } catch (e: JsonProcessingException) {
            return@GatewayFilter errorFilter.onError(exchange, e)
        } catch (e: GatewayException) {
            return@GatewayFilter errorFilter.onError(exchange, e)
        } catch (e: NullPointerException) {
            return@GatewayFilter errorFilter.onError(exchange, GatewayException(GatewayExceptionCode.BAD_REQUEST))
        }
    }

    companion object : Logger()
}