package puc.products.config

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import org.junit.jupiter.api.Test
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClientBuilder
import java.net.URI

class SecretsManagerLocalConfigTest {

    @Test
    fun `should return secrets manager client successfully`() {
        val region = Region.US_EAST_1
        val endpoint = "http://localhost:4566"
        val httpClient = mockk<ApacheHttpClient.Builder>()
        val credentials = AwsBasicCredentials.create(LOCAL_ACCESS_KEY, LOCAL_SECRET_ACCESS_KEY)
        val staticCredentialsProvider = mockk<StaticCredentialsProvider>()

        val secretsManagerClientBuilder = mockk<SecretsManagerClientBuilder>()
        val secretsManagerClient = mockk<SecretsManagerClient>()
        val overrideConfig = mockk<ClientOverrideConfiguration>()

        every {
            secretsManagerClientBuilder
                .httpClientBuilder(httpClient)
                .region(region)
                .endpointOverride(URI(endpoint))
                .credentialsProvider(staticCredentialsProvider)
                .overrideConfiguration(overrideConfig)
                .build()
        } returns secretsManagerClient

        mockkStatic(SecretsManagerClient::class, StaticCredentialsProvider::class, ClientOverrideConfiguration::class, ApacheHttpClient::class) {

            every { SecretsManagerClient.builder() } returns secretsManagerClientBuilder
            every { StaticCredentialsProvider.create(credentials) } returns staticCredentialsProvider
            every { ClientOverrideConfiguration.builder().build() } returns overrideConfig
            every { ApacheHttpClient.builder() } returns httpClient

            assertEquals(secretsManagerClient, SecretsManagerLocalConfig(endpoint).client)


            verify(exactly = 1) { SecretsManagerClient.builder() }
            verify(exactly = 1) { StaticCredentialsProvider.create(credentials) }
        }
        verify(exactly = 1) {
            secretsManagerClientBuilder
                .httpClientBuilder(httpClient)
                .region(region)
                .endpointOverride(URI(endpoint))
                .credentialsProvider(staticCredentialsProvider)
                .overrideConfiguration(overrideConfig)
                .build()
        }
    }


}