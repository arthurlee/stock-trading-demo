package com.boyal.demo.stocktrading.it

import com.boyal.demo.stocktrading.client.StockMarketClient
import com.boyal.demo.stocktrading.model.CurrencyRate
import com.boyal.demo.stocktrading.model.Stock
import com.boyal.demo.stocktrading.model.StockResponse
import com.boyal.demo.stocktrading.repository.StocksRepository
import org.junit.jupiter.api.assertNotNull
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ProblemDetail
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockIT {
    private val STOCK_ID = "621a97f1d11fc40fcdd5c67b"
    private val STOCK_NAME = "Globomantics"
    private val STOCK_PRICE = BigDecimal.TEN
    private val STOCK_CURRENCY = "USD"

    @MockitoBean
    private lateinit var stocksRepository : StocksRepository

    @MockitoBean
    private lateinit var stockMarketClient: StockMarketClient

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun shouldGetOneStock() {
        // GIVEN
        val stock = Stock(STOCK_ID, STOCK_NAME, STOCK_PRICE, STOCK_CURRENCY)
        val currencyRate = CurrencyRate("USD", BigDecimal.ONE)

        `when`(stocksRepository.findById(STOCK_ID)).thenReturn(Mono.just(stock))
        `when`(stockMarketClient.getCurrencyRates()).thenReturn(Flux.just(currencyRate))

        // WHEN
        val stockResponse = client.get()
            .uri { uriBuilder -> uriBuilder.path("/stocks/{id}").build(STOCK_ID) }
            .exchange()
            .expectStatus().isOk
            .expectBody(StockResponse::class.java)
            .returnResult()
            .responseBody

        // THEN
        assertNotNull(stockResponse)
        assertEquals(STOCK_ID, stockResponse.id)
        assertEquals(STOCK_NAME, stockResponse.name)
        assertEquals(STOCK_PRICE, stockResponse.price)
        assertEquals(STOCK_CURRENCY, stockResponse.currency)
    }

    @Test
    fun shouldReturnNotFoundWhenGetOneStock() {
        // GIVEN
        `when`(stocksRepository.findById(STOCK_ID)).thenReturn(Mono.empty())

        // WHEN
        val problemDetail = client.get()
            .uri { uriBuilder -> uriBuilder.path("/stocks/{id}").build(STOCK_ID) }
            .exchange()

        // THEN
            .expectStatus().isNotFound
            .expectBody(ProblemDetail::class.java)
            .returnResult()
            .responseBody

        assertNotNull(problemDetail)
        assertNotNull(problemDetail.detail)
        assertTrue { problemDetail.detail!!.contains("Stock not found") }
    }
}