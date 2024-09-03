package puc.infrastructure.configs

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Bean
    fun productRegisteredQueue(): Queue {
        return Queue("product.registered.queue", true)
    }

    @Bean
    fun productDeletedQueue(): Queue {
        return Queue("product.deleted.queue", true)
    }

    @Bean
    fun productExchange(): TopicExchange {
        return TopicExchange(exchange)
    }

    @Bean
    fun registeredBinding(): Binding {
        return BindingBuilder.bind(productRegisteredQueue()).to(productExchange()).with("product.registered")
    }

    @Bean
    fun deletedBinding(): Binding {
        return BindingBuilder.bind(productDeletedQueue()).to(productExchange()).with("product.deleted")
    }

    @Bean
    fun messageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = messageConverter()
        return template
    }
}