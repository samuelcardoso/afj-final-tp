package puc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication(exclude = [MongoAutoConfiguration::class])
class ProductsApplication {}

fun main(args: Array<String>) {
	runApplication<ProductsApplication>(*args)
}
