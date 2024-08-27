package puc.util

import org.springframework.stereotype.Component
import javax.crypto.Cipher
import javax.crypto.SecretKey
import java.util.Base64
import javax.crypto.spec.SecretKeySpec

@Component
class CipherUtil {
    val key: SecretKey = SecretKeySpec("secret".toByteArray(Charsets.UTF_8).copyOf(16), "AES")

/*  TODO: usar metodo para gerar segredo e guardar ele para uso futuro (substituir key por esse segredo gerado)
    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256) // Use a strong key size (e.g., 256 bits)
        return keyGenerator.generateKey()
    }
*/

    fun encryptPassword(password: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val passwordBytes = password.toByteArray(Charsets.UTF_8)
        val encryptedPassword = cipher.doFinal(passwordBytes)
        return Base64.getEncoder().encodeToString(encryptedPassword)
    }

    // Function to decrypt a password
    fun decryptPassword(encryptedPassword: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val encryptedPasswordBytes = Base64.getDecoder().decode(encryptedPassword)
        val passwordBytes = cipher.doFinal(encryptedPasswordBytes)
        return String(passwordBytes, Charsets.UTF_8)
    }
}