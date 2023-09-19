package com.gateway.api.config

import com.gateway.api.filter.AuthorizationHeaderFilter
import com.gateway.api.filter.RefreshTokenHeaderFilter
import com.gateway.api.util.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig(
    private val authorizationConfig: AuthorizationConfig,
) {
    @Value("\${gateway.server.user.uri}")
    lateinit var userServerUri: String

    @Bean
    fun setRoutes(
        builder: RouteLocatorBuilder,
        authorizationHeaderFilter: AuthorizationHeaderFilter,
        refreshTokenHeaderFilter: RefreshTokenHeaderFilter,
    ): RouteLocator {

        log.info("request")

        return builder.routes().route("user") { route ->
            route.path("/user/registration/social/**")
                .uri(userServerUri)

        }.route("user-reissue") { route ->
            route.path("/user/authentication/reissue/accesstoken")
                .filters { spec -> spec.filter(refreshTokenHeaderFilter.apply(authorizationConfig)) }
                .uri(userServerUri)

        }.route("user-general") { route ->
            route.path("/user/**")
                .filters { spec -> spec.filter(authorizationHeaderFilter.apply(authorizationConfig)) }
                .uri(userServerUri)

        }.build()
    }

    companion object : Logger()
}