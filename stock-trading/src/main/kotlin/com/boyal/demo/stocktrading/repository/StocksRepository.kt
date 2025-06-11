package com.boyal.demo.stocktrading.repository

import com.boyal.demo.stocktrading.model.Stock
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface StocksRepository: ReactiveMongoRepository<Stock, String>