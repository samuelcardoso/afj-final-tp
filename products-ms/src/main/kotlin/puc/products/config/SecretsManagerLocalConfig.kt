package puc.products.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import java.net.URI
import java.util.*

@Configuration
class SecretsManagerLocalConfig(
    @Value("\${aws.secrets-manager.route}")
    private val route: String
) : SecretsManagerClient {

    val logger = LoggerFactory.getLogger(this.javaClass)!!

    val client = SecretsManagerClient
        .builder()
        .httpClientBuilder(ApacheHttpClient.builder())
        .region(Region.US_EAST_1)
        .endpointOverride(URI(route))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(UUID.randomUUID().toString(), UUID.randomUUID().toString())
            )
        ).overrideConfiguration(ClientOverrideConfiguration.builder().build())
        .build()

    fun takeSecret(secretId: String): String? {

        return runCatching {
            client
                .getSecretValue(
                    GetSecretValueRequest
                        .builder()
                        .secretId(secretId)
                        .build()
                )
                .secretString()
        }.getOrElse {
            logger.error("It was not possible to take the secret under the id: $secretId", it)
            null
        }
    }

    override fun close() {}

    override fun serviceName(): String {
        return "secrets-manager"
    }

}