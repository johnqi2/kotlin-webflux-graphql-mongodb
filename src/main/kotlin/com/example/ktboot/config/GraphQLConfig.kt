package com.example.ktboot.config

import com.example.ktboot.repo.ProductRepo
import graphql.GraphQL
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import graphql.GraphQL.newGraphQL
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import java.io.InputStreamReader

@Configuration
class GraphQLConfig {
    @Autowired lateinit var resourceLoader: ResourceLoader
    @Value("classpath:schema.graphqls") lateinit var schemaResource: Resource
    @Autowired lateinit var productRepo: ProductRepo

    @Bean
    fun graphQL(): GraphQL {
        val resource = resourceLoader.getResource("classpath:/schema.graphqls")
        val typeRegistry = InputStreamReader(resource.inputStream).use {
            SchemaParser().parse(it.readText())
        }
        val schema = SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring())
        return newGraphQL(schema).build()
    }

    private fun runtimeWiring(): RuntimeWiring =
        RuntimeWiring.newRuntimeWiring()
            .type("Query") {
                it.dataFetcher("product") {
                    env -> productRepo.findById(env.arguments["sku"] as String).toFuture() }
            }
            .build()
}
