package com.example.ktboot.web

import com.example.ktboot.repo.ProductRepo
import com.example.ktboot.test.TestDataGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductHandlerTest {
    @Autowired
    lateinit var webClient: WebTestClient

    @Autowired
    lateinit var productRepo: ProductRepo

    @BeforeEach
    fun setUp() {
        productRepo.deleteAll()
        productRepo.save(TestDataGenerator.complexProduct())
            .block(Duration.ofSeconds(10))
    }

    @Test
    @DisplayName("Integration test for Product")
    fun getProductIT() {
        val prod = TestDataGenerator.complexProduct()

        webClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody("{ \"query\": \"{product(sku: \\\"${prod.sku}\\\") " +
                "{sku, shipping {weight}, details {type}}}\"}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data.product.sku").isEqualTo(prod.sku!!)
            .jsonPath("data.product.shipping[0].weight")
                .isEqualTo(prod.shipping!![0].weight!!)
            .jsonPath("data.product.details.type").isEqualTo(prod.details!!.type!!)
    }
}

