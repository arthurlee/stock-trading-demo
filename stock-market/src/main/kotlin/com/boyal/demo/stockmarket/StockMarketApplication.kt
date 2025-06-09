package com.boyal.demo.stockmarket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StockMarketApplication

fun main(args: Array<String>) {
    runApplication<StockMarketApplication>(*args)
}
