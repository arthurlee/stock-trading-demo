package com.boyal.demo.stockmarket.exception

import java.lang.RuntimeException

class StockPublishingException : RuntimeException {
    constructor(message: String) : super(message)
}