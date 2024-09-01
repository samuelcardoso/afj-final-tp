package puc.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(info=Info(title="Projeto USER-MS", version = "0.0.1",  description="User-ms api de usuário"))
class SwaggerConfig {
}