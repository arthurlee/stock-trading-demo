package com.boyal.demo.stockmarket.service

import com.boyal.demo.stockmarket.controller.dto.StockPublishRequest
import com.boyal.demo.stockmarket.controller.dto.StockPublishResponse
import com.boyal.demo.stockmarket.exception.StockPublishingException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class StockPublishingService {
    fun publishStock(request: StockPublishRequest): Mono<StockPublishResponse> {
        return Mono.just<StockPublishResponse>(StockPublishResponse(
            request.stockName,
            request.price,
            request.currencyName,
            getStatus(request),
        ))
    }

    fun getStatus(request: StockPublishRequest): String {
        if (request.stockName.contains("-")) {
            throw StockPublishingException("Stock name contains illegal character '-'")
        }
        return "SUCCESS"
    }
}