package com.boyal.demo.stockmarket.config

import com.boyal.demo.stockmarket.exception.StockPublishingException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class StockExceptionHandler {

    @ExceptionHandler(StockPublishingException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleStockPublishException(ex: StockPublishingException): ProblemDetail {
        val pd: ProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message)
        pd.title = "invalid stock name"
        return pd
    }
}