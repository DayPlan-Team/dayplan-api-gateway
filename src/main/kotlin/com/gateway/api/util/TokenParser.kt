package com.gateway.api.util

import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import java.util.*

object TokenParser {

    private const val TOKEN_PREFIX = "Bearer "

    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String

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

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(refinedToken).body
    }

    private fun removeTokenPrefix(token: String): String {
        return token.replace(TOKEN_PREFIX, "")
    }
}