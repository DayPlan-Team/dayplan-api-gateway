package com.gateway.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
class SecurityCustomConfig {

    companion object {
        private val WEB_SERVICE_WHITE_LIST = arrayOf(
            "/static/**", "/static/js/**", "/static/images/**",
            "/static/css/**", "/static/scss/**", "/static/docs/**",
            "/h2-console/**", "/favicon.ico", "/error"
        )

        private const val SCRIPT_SRC = "script-src 'self'"
    }

    @Bean
    fun applyWebSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers(*WEB_SERVICE_WHITE_LIST)
        }
    }

    @Bean
    fun applySecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange { exchanges ->
                exchanges.anyExchange().permitAll()
            }
            .securityContextRepository(StatelessWebSessionSecurityContextRepository())
            .csrf { csrf -> csrf.disable() }
            .headers {
                it.contentSecurityPolicy { exchange ->
                    exchange.policyDirectives(SCRIPT_SRC)
                }
            }

        return http.build()
    }

    class StatelessWebSessionSecurityContextRepository : ServerSecurityContextRepository {

        override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
            return Mono.empty()
        }

        override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
            return EMPTY_CONTEXT
        }

        companion object {
            private val EMPTY_CONTEXT = Mono.empty<SecurityContext>()
        }

    }

}