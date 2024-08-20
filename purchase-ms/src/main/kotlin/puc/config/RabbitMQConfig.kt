package puc.config

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun purchaseQueue(): Queue {
        return Queue("purchaseQueue", true)
    }
}