package com.boyal.demo.stocktrading.service

import com.boyal.demo.stockmarket.controller.dto.StockPublishRequest
import com.boyal.demo.stocktrading.client.StockMarketClient
import com.boyal.demo.stocktrading.exception.StockCreationException
import com.boyal.demo.stocktrading.exception.StockNotFoundException
import com.boyal.demo.stocktrading.model.Stock
import com.boyal.demo.stocktrading.model.StockRequest
import com.boyal.demo.stocktrading.model.StockResponse
import com.boyal.demo.stocktrading.repository.StocksRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.math.BigDecimal
import java.util.Currency

@Service
class StocksService {
    private final val log: Logger = LoggerFactory.getLogger(StocksService::class.java)
    private final val stocksRepository: StocksRepository
    private final val stocksMarketClient: StockMarketClient

    constructor(stocksRepository: StocksRepository, stocksMarketClient: StockMarketClient) {
        this.stocksRepository = stocksRepository
        this.stocksMarketClient = stocksMarketClient
    }

    fun getOneStock(id: String, currency: String): Mono<StockResponse> {
        return stocksRepository.findById(id)
            .flatMap { stock ->
                stocksMarketClient.getCurrencyRates()
                    .filter { currentRate -> currency.equals(currentRate.currentName, true) }
                    .singleOrEmpty()
                    .map { currencyRate -> StockResponse(stock.id, stock.name, stock.price, currency) }
                }
            .switchIfEmpty(Mono.error(StockNotFoundException( "Stock not found with id: " + id)))
            .doFirst { log.info("Retrieving stock with id: {}", id) }
            .doOnNext{stock -> log.info("Stock found: {}", stock) }
            .doOnError { ex -> log.error("Something went wrong while retrieving the stock with id: {}", id, ex) }
            .doOnTerminate {log.info("Finalized retrieving stock") }
            .doFinally {signalType -> log.info("Finalized retrieving stock with signal type: {}", signalType) }
    }

    fun getAllStocks(priceGreaterThan: BigDecimal): Flux<StockResponse> {
        return stocksRepository.findAll()
            .filter { stock -> stock.price.compareTo(priceGreaterThan) > 0 }
            .map(StockResponse::fromModel)
            .doFirst { log.info("Retrieving all stocks") }
            .doOnNext { stock -> log.info("Stock found: {}", stock) }
            .doOnError { ex -> log.warn("Something went wrong while retrieving the stocks", ex) }
            .doOnTerminate { log.info("Finalized retrieving stocks") }
            .doFinally {signalType -> log.info("Finalized retrieving stock with signal type: {}", signalType) }
    }

    fun createStock(stockRequest: StockRequest): Mono<StockResponse> {
        return Mono.just(stockRequest)
            .map(StockRequest::toModel)
            .flatMap { stock -> stocksRepository.save(stock) }
            .flatMap { stock -> stocksMarketClient.publishStock(generateStockPublishRequest(stockRequest))
                                    .filter { stockPublishResponse -> "SUCCESS".equals(stockPublishResponse.status, true) }
                                    .map { stockPublishResponse -> StockResponse.fromModel(stock) }
                                    .switchIfEmpty(Mono.error(StockCreationException("Unable to publish stock to the Stock Market")))
            }
            .onErrorMap {ex -> StockCreationException(ex.message.toString()) }
    }

    private fun generateStockPublishRequest(stockRequest: StockRequest): StockPublishRequest {
        return StockPublishRequest(stockRequest.name, stockRequest.price, stockRequest.currency)
    }
}