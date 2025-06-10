package com.boyal.demo.stocktrading.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

class StockCreationException: RuntimeException {
    constructor(message: String) : super(message)

    fun asProblemDetail(): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message)
        problemDetail.title = "Unable to Create Stock"
        return problemDetail
    }
}