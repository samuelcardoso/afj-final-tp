package puc.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    @Value("\${rabbitmq.queue}")
    lateinit var purchaseQueue: String

    @Value("\${rabbitmq.retry.ttl:60000}")
    var retryTtl: Long = 60000

    @Value("\${rabbitmq.retry.attempts:5}")
    var maxRetryAttempts: Int = 5

    @Bean
    fun dlqQueueName(): String {
        return "${purchaseQueue}_ERROR"
    }

    @Bean
    fun purchaseQueue(): Queue {
        val args = mapOf(
            "x-dead-letter-exchange" to exchange,
            "x-dead-letter-routing-key" to dlqQueueName(),
            "x-message-ttl" to retryTtl,
            "delivery-limit" to maxRetryAttempts
        )
        return Queue(purchaseQueue, true, false, false, args)
    }

    @Bean
    fun dlqQueue(): Queue {
        return Queue(dlqQueueName(), true)
    }

    @Bean
    fun exchange(): org.springframework.amqp.core.Exchange {
        return ExchangeBuilder.directExchange(exchange).durable(true).build()
    }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }

    @Bean
    fun purchaseBinding(): org.springframework.amqp.core.Binding {
        return BindingBuilder
            .bind(purchaseQueue())
            .to(exchange())
            .with(routingKey)
            .noargs()
    }

    @Bean
    fun dlqBinding(): org.springframework.amqp.core.Binding {
        return BindingBuilder
            .bind(dlqQueue())
            .to(exchange())
            .with(dlqQueueName())
            .noargs()
    }
}
