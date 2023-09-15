package com.gateway.api.util

import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.*

object TokenParser {

    private const val TOKEN_PREFIX = "Bearer "

    private val secretKey = System.getenv("JwtSecret").toByteArray()

    fun parseUserIdFromToken(token: String): String {
        val claims = parseToken(token)
        require(claims.expiration.time - Date().time >= 0) { throw GatewayException(GatewayExceptionCode.INVALID_REQUEST) }

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