package com.gateway.api.filter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.gateway.api.advice.ExceptionResponse
import com.gateway.api.exception.exception.GatewayException
import com.gateway.api.exception.exception.GatewayExceptionCode
import com.gateway.api.util.Logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class ErrorFilter {
    fun onError(exchange: ServerWebExchange, exception: Exception): Mono<Void> {
        val response = exchange.response
        log.info("exception = $exception")

        val exceptionResponse: ExceptionResponse

        when(exception) {
            is JsonProcessingException -> {
                response.setStatusCode(HttpStatus.BAD_REQUEST)
                exceptionResponse = ExceptionResponse(
                    status = GatewayExceptionCode.BAD_REQUEST.status,
                    code = GatewayExceptionCode.BAD_REQUEST.errorCode,
                    message = GatewayExceptionCode.BAD_REQUEST.message
                )
            }
            is GatewayException -> {
                val status = HttpStatus.valueOf(exception.status)
                response.setStatusCode(status)
                exceptionResponse = ExceptionResponse(
                    status = status.value(),
                    code = exception.errorCode,
                    message = exception.message
                )
            }
            else -> {
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                exceptionResponse = ExceptionResponse(
                    status = GatewayExceptionCode.SERVER_ERROR.status,
                    code = GatewayExceptionCode.BAD_REQUEST.errorCode,
                    message = GatewayExceptionCode.BAD_REQUEST.message
                )

            }
        }

        val json = ObjectMapper().writeValueAsString(exceptionResponse)  // Convert ExceptionResponse to JSON string
        val bufferFactory = exchange.response.bufferFactory()
        val wrappedBuffer = bufferFactory.wrap(json.toByteArray())

        log.info("json = $json")

        return response.writeWith(Mono.just(wrappedBuffer))
    }

    companion object : Logger()

}