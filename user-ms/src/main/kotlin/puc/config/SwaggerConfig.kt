package puc.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Projeto USER-MS",
        version = "0.0.1",
        description = "User-ms API de usuário",
        termsOfService = "http://swagger.io/terms/",
        contact = Contact(
            name = "Nome do contato",
            url = "http://www.example.com/contact",
            email = "contact@example.com"
        ),
        license = License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    )
)
class SwaggerConfig {
}