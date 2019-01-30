package com.example.ktboot.test

import com.example.ktboot.model.Details
import com.example.ktboot.model.Product
import com.example.ktboot.model.Shipping

object TestDataGenerator {

    fun complexProduct(): Product {
        val theDetails = Details()
        with(theDetails) {
            title = "Smart HDTV with streaming built in"
            type = "HDTV"
            tracks = listOf("tracks", "Smart TV", "HDTV", "Rouku")
        }
        val theDimension = Shipping.Dimension()
        with(theDimension) {
            width = 100
            height = 60
            depth = 50
        }
        val theShipping = Shipping()
        with(theShipping) {
            weight = 28
            dimensions = theDimension
        }
        val product = Product()
        with(product) {
            sku = "sku8585"
            title = "XYZ 32S327 Smart LED TV"
            description = "1080 Full HD TV with build in streaming"
            shipping = listOf(theShipping)
            details = theDetails
        }
        return product
    }
}

