package com.example.ktboot.config

import com.example.ktboot.web.CustomErrorAttributes
import org.springframework.context.annotation.Configuration
import com.example.ktboot.web.GlobalExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order

@Configuration
class AppConfig {
    @Bean
    fun errorAttributes() = CustomErrorAttributes()

    @Bean
    @Order(-1)
    fun errorWebExceptionHandler(
        codeConfigurer: ServerCodecConfigurer,
        errorAttributes: ErrorAttributes
    ): ErrorWebExceptionHandler = GlobalExceptionHandler(codeConfigurer, errorAttributes)
}

