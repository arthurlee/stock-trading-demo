package com.boyal.demo.stocktrading.client

import com.boyal.demo.stockmarket.controller.dto.StockPublishRequest
import com.boyal.demo.stockmarket.controller.dto.StockPublishResponse
import com.boyal.demo.stocktrading.exception.StockCreationException
import com.boyal.demo.stocktrading.model.CurrencyRate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class StockMarketClient {
    private final val log: Logger = LoggerFactory.getLogger(StockMarketClient::class.java)

    private final val webClient: WebClient

    constructor(
        @Value("\${clients.stockMarket.baseUrl}") baseUrl: String
    ) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter(ExchangeFilterFunction.ofRequestProcessor {
                    request -> Mono.just(
                    ClientRequest.from(request)
                            .header("X-Trace-Id", UUID.randomUUID().toString())
                            .build())
                })
            .build()
    }

    fun getCurrencyRates(): Flux<CurrencyRate> {
        return webClient.get()
            .uri("/currency/rates")
            .retrieve()
            .bodyToFlux(CurrencyRate::class.java)
            .doFirst { log.info("Calling GET Currency Rates API") }
            .doOnNext { cr -> log.info("GET Currency Rates API Response: {}", cr) }
    }

    fun publishStock(stockPublishRequest: StockPublishRequest): Mono<StockPublishResponse> {
        return webClient.post()
            .uri("/stocks/publish")
            .body(BodyInserters.fromValue(stockPublishRequest))
            .exchangeToMono { response ->
                if (! response.statusCode().isError) {
                    response.bodyToMono(StockPublishResponse::class.java)
                } else {
                    response.bodyToMono(ProblemDetail::class.java)
                        .flatMap { errorMessage ->
                            Mono.error(StockCreationException(errorMessage.detail.toString()))
                        }
                }
            }
            .doFirst { log.info("Calling Publish Stock API with Request Body: {}", stockPublishRequest) }
            .doOnNext { spr -> log.info("Publish Stock API Response: {}", spr) }
    }
}