package puc.products.config

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MongoDbConfigTest{

    @Test
    fun `should create mongo config successfully`(){
        val uriToBeReplaced = "mongodb://user:{pass}@localhost:27017/productsdb"

        val secretsLocal = mockk<SecretsManagerLocalConfig>()


        every {
            secretsLocal.takeSecret("ProductsDatabase")
        } returns "my-unit-test"


        assertDoesNotThrow {
            MongoDbConfig(secretsLocal, secretId = "ProductsDatabase", dbRoute =  uriToBeReplaced, dbBaseName = "ProductsDatabase").mongoTemplate()
        }
    }
}