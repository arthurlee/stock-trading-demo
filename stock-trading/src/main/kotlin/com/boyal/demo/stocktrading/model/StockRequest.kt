package com.boyal.demo.stocktrading.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.util.UUID

data class StockRequest(
    @JsonProperty("stockName")
    val name: String,
    val price: BigDecimal,
    val currency: String
) {
    fun toModel(): Stock {
        return Stock(UUID.randomUUID().toString(), name, price, currency)
    }
}
