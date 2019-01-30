package com.example.ktboot.web

import mu.KotlinLogging
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.http.MediaType
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.http.codec.ServerCodecConfigurer

private val logger = KotlinLogging.logger {}

class GlobalExceptionHandler(
    private val codecConfigurer: ServerCodecConfigurer,
    private val errorAttributes: ErrorAttributes
) : ErrorWebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        if (exchange.response.isCommitted) {
            return Mono.error(ex)
        }
        errorAttributes.storeErrorInformation(ex, exchange)
        val request = ServerRequest.create(exchange, codecConfigurer.readers)
        return handleRequest(request).flatMap { write(exchange, it) }
    }

    private fun write(exchange: ServerWebExchange, response: ServerResponse): Mono<out Void> {
        exchange.response.headers.contentType = response.headers().contentType
        return response.writeTo(exchange, object: ServerResponse.Context {
            override fun messageWriters(): List<HttpMessageWriter<*>> = codecConfigurer.writers
            override fun viewResolvers(): List<ViewResolver> = emptyList()
        })
    }

    private fun handleRequest(req: ServerRequest): Mono<ServerResponse> {
        val errorMap = errorAttributes.getErrorAttributes(req, false)
        val errorStatus =
            (errorAttributes as CustomErrorAttributes).determineHttpStatus(req)

        return ServerResponse.status(errorStatus)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(BodyInserters.fromObject<Any>(errorMap))
            .doOnNext { log(req, errorMap) }
    }

    private fun log(req: ServerRequest, errorMap: Map<String, Any>) {
        val ex = errorAttributes.getError(req)
        val uri = req.uri().toString()
        val code = errorMap["code"] as Int
        if (code in 500..600) {
            logger.error(ex) { "Request uri: $uri. Response: $errorMap." }
        } else {
            logger.error { "Request uri: $uri. Response: $errorMap." }
        }
    }
}
