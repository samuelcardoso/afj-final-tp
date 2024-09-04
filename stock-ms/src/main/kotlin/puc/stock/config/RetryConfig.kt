package puc.stock.config

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import puc.stock.annotation.Retry
import java.lang.reflect.Method


@Aspect
@Component
class RetryConfig {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Around("@annotation(puc.stock.annotation.Retry)")
    fun retry(call: ProceedingJoinPoint) : Any? {
        var response: Any? = null
        val method = (call.signature as MethodSignature).method as Method
        val retryAnnotation = method.getAnnotation(Retry::class.java)
        val maximum = retryAnnotation.times
        var actualRetry = 0
        var isSuccessInvoke = false

        do {
            try {
                response = call.proceed()
                isSuccessInvoke = true
            } catch (ex: Exception) {
                logger.info("==== Erro na chamada do metódo [{}]", method.name)
                if (actualRetry == maximum) {
                    throw ex
                }

                actualRetry += 1
                logger.info("==== Efetuando retentativa [{}] de [{}] após [5] segundos.....", actualRetry, maximum)
                Thread.sleep(2000)
            }
        } while (!isSuccessInvoke)

        return response
    }
}