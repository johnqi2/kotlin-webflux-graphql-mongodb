 
#### Introduction
A demo of kotlin Reactive GraphQL API using reactive technology stacks, including Spring WebFlux, spring-boot-starter-webflux, graphql-java-spring-boot-starter-webflux, spring-boot-starter-data-mongodb-reactive, and MongoDB reactive streams.
 
#### Technology Stacks
* Java 8
* Kotlin
* Spring 
  * Spring WebFlux
  * Spring-boot 2.1.*
  * spring-boot-starter-data-mongodb-reactive
* graphql-java-spring-boot-starter-webflux
* GraphQL
* MongoDB, MongoDB Reactive Stream
* Embedded Mongo
* Maven, Gradle
* Junit 5, Mokito
#### Instruction
* To run the project, make sure the MongoDB Community Server 4.0+ installed locally.
* Testing data is first populated to DB if not exist.
```
maven spring-boot:run
or
./gradlew bootRun
```
* curl command for quick testing
```
* query using GET method
// encoded query prameters: {product(sku:"sku8585"){sku,shipping{weight},details{type}}}
curl -i 'http://localhost:9080/graphql?query=%7Bproduct%28sku%3A%22sku8585%22%29%7Bsku%2Cshipping%7Bweight%7D%2Cdetails%7Btype%7D%7D%7D'

* query using POST method
curl -i -XPOST 'http://localhost:9080/graphql' \
     -H "Content-Type: application/json" \
     --data '{ "query": "{product(sku: \"sku8585\") {sku, shipping {weight}, details {type}}}"}'

```
#### NOTE
How to customerize the GraphQL service endpoints, please refer to [GraphQL Java Spring](https://github.com/graphql-java/graphql-java-springgraphql-java-spring-boot-starter-webflux)
