package puc

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import feign.codec.ErrorDecoder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean


@SpringBootApplication(exclude = [MongoAutoConfiguration::class])
@EnableFeignClients
class ProductsApplication {}

fun main(args: Array<String>) {
	runApplication<ProductsApplication>(*args)
}

@Bean
fun objectMapper(): ObjectMapper {
	val objectMapper = ObjectMapper()
	objectMapper.registerModule(JavaTimeModule())
	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	return objectMapper
}