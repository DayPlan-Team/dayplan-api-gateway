package com.gateway.api.util

enum class HeaderType(
    val type: String,
) {
    AUTHORIZATION_HEADER("Authorization"),
    REFRESHTOKEN_HEADER("RefreshToken")
}