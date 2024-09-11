import jakarta.validation.Constraint
import jakarta.validation.Payload
import puc.annotation.CpfCnpjValidator
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [CpfCnpjValidator::class])
@Target(allowedTargets = [AnnotationTarget.FIELD])
@Retention(AnnotationRetention.RUNTIME)

annotation class CpfCnpj(
    val message: String = "Documento inválido",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = [],
)