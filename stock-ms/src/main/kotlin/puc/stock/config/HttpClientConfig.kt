package puc.stock.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import puc.stock.client.ProductClient

@Configuration
class HttpClientConfig {

    @Bean
    fun productClient(): ProductClient {
        val webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/products")
                .build()

        val factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
                .build()

        return factory.createClient(ProductClient::class.java)
    }
}