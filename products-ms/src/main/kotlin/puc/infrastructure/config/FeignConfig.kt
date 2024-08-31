package puc.infrastructure.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig {

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        val interceptor = RequestInterceptor { template -> template?.header("Authorization", HeaderContext.getHeader()) }
        return interceptor;
    }

}