package com.boyal.demo.stockmarket.controller

import com.boyal.demo.stockmarket.model.CurrencyRate
import com.boyal.demo.stockmarket.service.CurrencyRateService
import lombok.extern.slf4j.Slf4j
import lombok.`val`
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@Slf4j
class StockMarketController {
    private final val currencyRateService: CurrencyRateService

    constructor( cs: CurrencyRateService) {
        this.currencyRateService = cs
    }

    @GetMapping("/currencyrates")
    fun getCurrentRates(
        @RequestHeader("X-Trace-Id", required = false)  traceId: String?
    ): Flux<CurrencyRate> {
        return currencyRateService.getCurrencyRates()
    }
}