package com.boyal.demo.stockmarket.controller.dto

import java.math.BigDecimal

data class StockPublishRequest(
    val stockName: String,
    val price: BigDecimal,
    val currencyName: String,
)
