package puc.infrastructure.config

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserClientConfig {

    @Bean
    fun feignRequestInterceptor(): RequestInterceptor {
        val interceptor = RequestInterceptor { template -> template?.header("Authorization", HeaderContext.getHeader()) }
        return interceptor;
    }

}