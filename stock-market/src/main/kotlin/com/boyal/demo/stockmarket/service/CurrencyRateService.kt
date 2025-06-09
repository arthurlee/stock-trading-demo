package com.boyal.demo.stockmarket.service

import com.boyal.demo.stockmarket.model.CurrencyRate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.math.BigDecimal

@Service
class CurrencyRateService {
    final val rates = listOf(
        CurrencyRate("USD", BigDecimal.ONE),
        CurrencyRate("EUR", BigDecimal.valueOf(1.15)),
        CurrencyRate("CAD", BigDecimal.valueOf(0.8)),
        CurrencyRate("AUD", BigDecimal.valueOf(0.75)),
    )

    fun getCurrencyRates(): Flux<CurrencyRate> {
        return Flux.fromIterable(rates)
    }
}
