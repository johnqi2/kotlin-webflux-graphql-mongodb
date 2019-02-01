package com.example.ktboot.config

import com.example.ktboot.repo.ProductRepo
import com.example.ktboot.test.TestDataGenerator
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * A loader to populate MongoDB testing data while app startup.
 */
@Component
@Profile("dev")
class DataLoader(private val productRepo: ProductRepo) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val product = TestDataGenerator.complexProduct()
        productRepo.findById(product.sku!!)
            .switchIfEmpty(
                productRepo.save(product)
            )
            .subscribe()
    }
}
