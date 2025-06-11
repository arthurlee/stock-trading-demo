import com.boyal.demo.stockmarket.controller.dto.StockPublishResponse
import com.boyal.demo.stocktrading.client.StockMarketClient
import com.boyal.demo.stocktrading.model.Stock
import com.boyal.demo.stocktrading.model.StockRequest
import com.boyal.demo.stocktrading.repository.StocksRepository
import com.boyal.demo.stocktrading.service.StocksService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.MockitoAnnotations
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class StocksServiceTest {

    private val STOCK_ID = "621a97f1d11fc40fcdd5c67b"
    private val STOCK_NAME = "Globomantics"
    private val STOCK_PRICE = BigDecimal.TEN
    private val STOCK_CURRENCY = "USD"

    @Mock
    private lateinit var stocksRepository : StocksRepository

    @Mock
    private lateinit var stockMarketClient: StockMarketClient

    @InjectMocks
    private lateinit var stocksService: StocksService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun shouldCreateStock() {
        // GIVEN
        val stockRequest = StockRequest(STOCK_NAME, STOCK_PRICE, STOCK_CURRENCY)
        val stock = Stock(STOCK_ID, STOCK_CURRENCY, STOCK_PRICE, STOCK_CURRENCY)
        val stockPublishResponse = StockPublishResponse(STOCK_ID, STOCK_PRICE, STOCK_CURRENCY, "SUCCESS")

        `when`(stocksRepository.save(any())).thenReturn(Mono.just(stock))
        `when`(stockMarketClient.publishStock(any())).thenReturn(Mono.just(stockPublishResponse))

        // WHEN
        StepVerifier.create(stocksService.createStock(stockRequest) )
        // THEN
            .assertNext { stockResponse -> {
                assertNotNull(stockResponse)
                assertEquals(STOCK_ID, stockResponse.id)
                assertEquals(STOCK_NAME, stockResponse.name)
                assertEquals(STOCK_PRICE, stockResponse.price)
                assertEquals(STOCK_CURRENCY, stockResponse.currency)
            } }
            .verifyComplete()
    }
}