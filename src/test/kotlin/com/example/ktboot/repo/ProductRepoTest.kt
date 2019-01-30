package com.example.ktboot.repo

import com.example.ktboot.TestData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import reactor.test.StepVerifier

@DataMongoTest
class ProductRepoTest {
    @Autowired
    lateinit var productRepo: ProductRepo
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Test
    fun testFindById() {
        val sku = "sku1"
        val product = TestData.product(sku)
        mongoTemplate.save(product)

        StepVerifier
            .create(productRepo.findById(sku))
            .expectNextMatches { sku == it.sku }
            .verifyComplete()
    }
}
