package com.boyal.demo.stocktrading.controller

import com.boyal.demo.stocktrading.model.StockRequest
import com.boyal.demo.stocktrading.model.StockResponse
import com.boyal.demo.stocktrading.service.StocksService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal


@RestController
@RequestMapping("/stocks")
class StocksController {
    private final val log: Logger = LoggerFactory.getLogger(StocksController::class.java)

    private final val stocksService: StocksService

    constructor(stocksService: StocksService) {
        this.stocksService = stocksService
    }

    @GetMapping("/{id}")
    fun getOneStock(
        @PathVariable id: String,
        @RequestParam(required = false, defaultValue = "USD") currency : String
    ): Mono<StockResponse> {
        log.info("getOneStock")
        return stocksService.getOneStock(id, currency)
    }

    @GetMapping
    fun getAllStocks(
        @RequestParam(required = false, defaultValue = "0") priceGreaterThan: BigDecimal
    ): Flux<StockResponse> {
        log.info("getAllStocks")
        return stocksService.getAllStocks(priceGreaterThan)
    }

    @PostMapping
    fun createStock(
        @RequestBody stockRequest: StockRequest
    ): Mono<StockResponse> {
        log.info("createStock")
        return stocksService.createStock(stockRequest)
    }
}