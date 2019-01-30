package com.example.ktboot.repo

import com.example.ktboot.model.Product
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepo : ReactiveMongoRepository<Product, String>
