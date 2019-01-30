package com.example.ktboot.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Product {
    @Id
    var sku: String? = null

    var type: String? = null
    var title: String? = null
    var description: String? = null
    var shipping: List<Shipping>? = null
    var details: Details? = null
}

class Shipping {
    var weight: Int? = null
    var dimensions: Dimension? = null

    class Dimension {
        var width: Int? = null
        var height: Int? = null
        var depth: Int? = null
    }
}

class Details {
    var title: String? = null
    var type: String? = null
    var tracks: List<String>? = null
}


