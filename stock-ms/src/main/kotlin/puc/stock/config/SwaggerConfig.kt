package puc.stock.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("kotlin.puc.stock")
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun apiInfo(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("API de controle de estoque de produtos")
                    .contact(
                        Contact().url("https://github.com/Maxel-Uds/afj-final-tp")
                    )
                    .description("Esta API atua na operação de atualização do estoque de um produto")
                    .version("v0.0.1")
                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
            )
    }
}