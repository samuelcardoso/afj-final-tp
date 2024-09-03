package puc.infrastructure.configs

import feign.RequestInterceptor
import feign.Retryer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class FeignConfig {

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        val interceptor = RequestInterceptor { template -> template?.header("Authorization", HeaderContext.getHeader()) }
        return interceptor;
    }

    @Bean
    fun retryer(): Retryer {
        return Retryer.Default(100, TimeUnit.SECONDS.toMillis(3), 5)
    }

}