package com.boyal.demo.stocktrading.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class StockRequest(
    @JsonProperty("stockName")
    val name: String,
    val price: BigDecimal,
    val currency: String
) {
    fun toModel(): Stock {
        return Stock("", name, price, currency)
    }
}
