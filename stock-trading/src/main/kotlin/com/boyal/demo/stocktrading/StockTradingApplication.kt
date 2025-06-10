package com.boyal.demo.stocktrading

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StockTradingApplication

fun main(args: Array<String>) {
    runApplication<StockTradingApplication>(*args)
}
