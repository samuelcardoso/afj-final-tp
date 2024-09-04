package puc.products.config

import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
class MongoDbConfig(
    secretsManagerLocalConfig: SecretsManagerLocalConfig,
    @Value("\${spring.data.mongodb.secretId}")
    private val secretId: String,

    @Value("\${spring.data.mongodb.dbName}")
    private val dbBaseName: String,
    @Value("\${spring.data.mongodb.uri}")
    private val dbRoute: String
) {
    private val dbPassword: String = secretsManagerLocalConfig.takeSecret(secretId)

    @Bean
    fun mongoTemplate(): MongoTemplate {
        val connectionString = dbRoute.replace("{pass}", dbPassword)
        val client = MongoClients.create(connectionString)

        return MongoTemplate(SimpleMongoClientDatabaseFactory(client, dbBaseName))
    }

}