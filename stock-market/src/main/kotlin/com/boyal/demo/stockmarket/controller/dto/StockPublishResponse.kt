package com.boyal.demo.stockmarket.controller.dto

import java.math.BigDecimal

data class StockPublishResponse(
    val stockName: String,
    val price: BigDecimal,
    val currencyName: String,
    val status: String,
)
