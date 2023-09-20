package com.gateway.api.advice

data class ExceptionResponse(
    val status: Int,
    val code: String,
    val message: String,
)
