package puc.stock.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Retry(
    val times: Int = 1
)
