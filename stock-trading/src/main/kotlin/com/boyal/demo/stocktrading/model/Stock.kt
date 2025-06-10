package com.boyal.demo.stocktrading.model

import java.math.BigDecimal

data class Stock(
    val id: String,
    val name: String,
    val price: BigDecimal,
    val currency: String,
)
