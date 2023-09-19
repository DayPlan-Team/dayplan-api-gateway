package com.gateway.api.util

import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class TokenParser : InitializingBean {

    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String

    private lateinit var key: Key

    override fun afterPropertiesSet() {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }


    fun parseUserIdFromToken(token: String, headerType: HeaderType): String {
        val claims = parseToken(token)

        when (headerType) {
            HeaderType.AUTHORIZATION_HEADER -> require(claims.expiration.time - Date().time >= 0) {
                throw GatewayException(
                    GatewayExceptionCode.ACCESSTOKEN_EXPIRED,
                )
            }

            HeaderType.REFRESHTOKEN_HEADER -> require(claims.expiration.time - Date().time >= 0) {
                throw GatewayException(
                    GatewayExceptionCode.REFRESHTOKEN_EXPIRED,
                )
            }
        }

        return claims.subject
    }

    private fun parseToken(token: String): Claims {
        val refinedToken = removeTokenPrefix(token)
        log.info("refinedToken = $refinedToken")

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refinedToken).body
    }

    private fun removeTokenPrefix(token: String): String {
        return token.replace(TOKEN_PREFIX, "")
    }

    companion object : Logger() {
        private const val TOKEN_PREFIX = "Bearer "
    }
}