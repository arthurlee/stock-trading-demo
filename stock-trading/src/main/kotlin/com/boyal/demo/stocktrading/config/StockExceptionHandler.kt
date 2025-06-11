package com.boyal.demo.stocktrading.config

import com.boyal.demo.stocktrading.exception.StockCreationException
import com.boyal.demo.stocktrading.exception.StockNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class StockExceptionHandler {

    @ExceptionHandler(StockCreationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleStockPublishException(ex: StockCreationException): ProblemDetail {
        return ex.asProblemDetail()
    }

    @ExceptionHandler(StockNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleStockPublishException(ex: StockNotFoundException): ProblemDetail {
        val pd: ProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
        pd.title = "Stock Not Found"
        return pd
    }
}