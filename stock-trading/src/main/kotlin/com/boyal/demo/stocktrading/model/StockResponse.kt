package com.boyal.demo.stocktrading.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class StockResponse(
    val id: String,

    @JsonProperty("stockName")
    val name: String,

    val price: BigDecimal,
    val currency: String
) {
    companion object {
        @JvmStatic
        fun fromModel(stock: Stock) : StockResponse {
            return StockResponse(stock.id, stock.name, stock.price,stock.currency)
        }
    }
}
