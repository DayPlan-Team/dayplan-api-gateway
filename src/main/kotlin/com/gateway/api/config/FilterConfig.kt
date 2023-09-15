package com.gateway.api.config

import com.gateway.api.filter.AuthorizationHeaderFilter
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
    fun setRoutes(builder: RouteLocatorBuilder, authorizationHeaderFilter: AuthorizationHeaderFilter): RouteLocator {
        return builder.routes().route("users") { route ->
                route.path("/user-server/users")
                    .filters { spec -> spec.filter(authorizationHeaderFilter.apply(authorizationConfig)) }
                    .uri(userServerUri)
            }.build()
    }
}