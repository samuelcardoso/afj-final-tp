package puc.config

import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
//import org.springframework.amqp.rabbit.listener.SimpleRabbitListenerContainerFactory
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    @Value("\${rabbitmq.queue}")
    lateinit var purchaseQueue: String

    @Bean
    fun purchaseQueue(): Queue {
        return Queue(purchaseQueue, true)
    }

    @Bean
    fun exchange(): org.springframework.amqp.core.Exchange {
        return ExchangeBuilder.directExchange(exchange).durable(true).build()
    }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }

//    @Bean
//    fun simpleRabbitListenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
//        val retryTemplate = RetryTemplate()
//        val retryPolicy = SimpleRetryPolicy(3)
//        retryTemplate.retryPolicy = retryPolicy
//
//        val factory = SimpleRabbitListenerContainerFactory()
//        factory.connectionFactory = connectionFactory
//        factory.setConcurrentConsumers(3)
//        factory.setMaxConcurrentConsumers(10)
//        factory.setPrefetchCount(1)
//        factory.setRetryTemplate(retryTemplate)
//        return factory
//    }

    @Bean
    fun binding(): org.springframework.amqp.core.Binding {
        return org.springframework.amqp.core.BindingBuilder
            .bind(purchaseQueue())
            .to(exchange())
            .with(routingKey)
            .noargs()
    }
}