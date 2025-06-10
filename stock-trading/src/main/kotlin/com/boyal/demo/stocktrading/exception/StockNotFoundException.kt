package com.boyal.demo.stocktrading.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

class StockNotFoundException: RuntimeException {
    constructor(message: String) : super(message)
}