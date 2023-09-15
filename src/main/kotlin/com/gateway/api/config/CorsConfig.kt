package com.gateway.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()

        corsConfiguration.allowCredentials = true
        corsConfiguration.addAllowedOriginPattern(LOCAL_HOST)
        corsConfiguration.addAllowedOriginPattern(ALL)
        corsConfiguration.addAllowedHeader(ALL)
        corsConfiguration.addAllowedMethod(ALL)
        corsConfiguration.addExposedHeader(EXPOSED_HEADER)

        source.registerCorsConfiguration(SOURCE_PATTERN, corsConfiguration)
        return CorsFilter(source)
    }

    companion object {
        const val LOCAL_HOST = "http://localhost:8000"
        const val ALL = "*"
        const val EXPOSED_HEADER = "Access-Control-Allow-Origin"
        const val SOURCE_PATTERN = "/**"
    }

}