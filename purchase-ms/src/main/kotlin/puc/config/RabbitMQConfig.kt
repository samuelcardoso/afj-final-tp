package puc.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


@Configuration
class RabbitMQConfig {
    @Autowired
    private val connectionFactory: ConnectionFactory? = null

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

    @Bean
    fun binding(): org.springframework.amqp.core.Binding {
        return org.springframework.amqp.core.BindingBuilder
            .bind(purchaseQueue())
            .to(exchange())
            .with(routingKey)
            .noargs()
    }

    @Bean
    fun jacksonConverter(): Jackson2JsonMessageConverter {
        val mapper = Jackson2ObjectMapperBuilder
            .json()
            .modules(JavaTimeModule())
            .dateFormat(StdDateFormat())
            .build<ObjectMapper>()
        return Jackson2JsonMessageConverter(mapper)
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jacksonConverter()
        return rabbitTemplate
    }
}