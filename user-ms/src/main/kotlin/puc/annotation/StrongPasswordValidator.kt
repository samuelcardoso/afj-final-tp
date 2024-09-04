package puc.annotation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class StrongPasswordValidator : ConstraintValidator<StrongPassword, String> {
    override fun isValid(password: String?, context: ConstraintValidatorContext?): Boolean {
        if (password.isNullOrBlank()) return false

        val minLength = 8
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return password.length >= minLength && hasLetter && hasDigit && hasSpecialChar
    }
}