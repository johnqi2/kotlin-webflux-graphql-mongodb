package com.example.ktboot.web

import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import java.util.LinkedHashMap
import javax.validation.ConstraintViolationException

private val ERROR_ATTRIBUTE = CustomErrorAttributes::class.java.name + ".ERROR"

class CustomErrorAttributes : ErrorAttributes {
    override fun storeErrorInformation(error: Throwable, exchange: ServerWebExchange?) {
        exchange!!.attributes.putIfAbsent(ERROR_ATTRIBUTE, error)
    }

    override fun getErrorAttributes(
        request: ServerRequest,
        includeStackTrace: Boolean
    ): MutableMap<String, Any> {
        val errorMap = LinkedHashMap<String, Any>()
        val error = getError(request)
        errorMap["code"] = determineHttpStatus(request).value()
        errorMap["message"] = error.message as String
        return errorMap
    }

    override fun getError(request: ServerRequest): Throwable {
        return request.attribute(ERROR_ATTRIBUTE)
            .orElseThrow { IllegalStateException("Missing exception") } as Throwable
    }

    fun determineHttpStatus(request: ServerRequest): HttpStatus {
        val error = getError(request)
        return when (error) {
            is ResponseStatusException -> error.status
            is ConstraintViolationException -> HttpStatus.BAD_REQUEST
            is DecodingException -> HttpStatus.BAD_REQUEST
            else -> {
                val status = AnnotatedElementUtils.findMergedAnnotation(
                    error.javaClass, ResponseStatus::class.java
                )
                status?.code ?: HttpStatus.INTERNAL_SERVER_ERROR
            }
        }
    }
}
