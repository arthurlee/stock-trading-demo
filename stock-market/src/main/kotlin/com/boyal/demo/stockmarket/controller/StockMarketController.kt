package com.boyal.demo.stockmarket.controller

import com.boyal.demo.stockmarket.controller.dto.StockPublishRequest
import com.boyal.demo.stockmarket.controller.dto.StockPublishResponse
import com.boyal.demo.stockmarket.model.CurrencyRate
import com.boyal.demo.stockmarket.service.CurrencyRateService
import com.boyal.demo.stockmarket.service.StockPublishingService
import org.slf4j.Logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import  org.slf4j.LoggerFactory

@RestController
//@Slf4j
class StockMarketController {
    private final val log: Logger = LoggerFactory.getLogger(StockMarketController::class.java)

    private final val currencyRateService: CurrencyRateService
    private final val stockPublishingService: StockPublishingService

    constructor( cs: CurrencyRateService, sp : StockPublishingService) {
        this.currencyRateService = cs
        this.stockPublishingService = sp
    }

    @GetMapping("/currencyrates")
    fun getCurrentRates(
        @RequestHeader("X-Trace-Id", required = false)  traceId: String?
    ): Flux<CurrencyRate> {
        log.info("Get Currency Rates API called with Trace Id: {}", traceId)
        return currencyRateService.getCurrencyRates()
    }

    @PostMapping("/stocks/publish")
    fun publishStock(
        @RequestBody request: StockPublishRequest,
        @RequestHeader("X-Trace-Id", required = false)  traceId: String?): Mono<StockPublishResponse> {
        log.info("Publish Stock API called with Trace Id: {}", traceId)
        return  stockPublishingService.publishStock(request)
    }
}