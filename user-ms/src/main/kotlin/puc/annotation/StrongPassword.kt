package puc.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [StrongPasswordValidator::class])
annotation class StrongPassword(
    val message: String = "Password must be at least 8 characters long, with at least 1 letter, 1 number, and 1 special character",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)