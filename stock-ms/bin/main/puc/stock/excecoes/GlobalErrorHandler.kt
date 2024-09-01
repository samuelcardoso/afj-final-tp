package puc.stock.excecoes


import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Component
@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {

     fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        val logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
        logger.error("MethodArgumentNotValidException observed : ${ex.message}", ex)
        val errors = ex.bindingResult.allErrors
                .map { error -> error.defaultMessage!! }
                .sorted()

        logger.info("errors : $errors")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors.joinToString(", ") { it})
    }

    @ExceptionHandler(StockNotFoundException::class)
    fun handleStockNotFoundExceptions(ex: StockNotFoundException, request: WebRequest) : ResponseEntity<Any> {
        logger.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.message)
    }

    @ExceptionHandler(StockAlreadyRegisteredException::class)
    fun handleStockAlreadyRegisteredExceptions(ex: StockAlreadyRegisteredException, request: WebRequest) : ResponseEntity<Any> {
        logger.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.IM_USED)
                .body(ex.message)
    }

    @ExceptionHandler(InsufficientStockException::class)
    fun handleInsufficientStockExceptions(ex: InsufficientStockException, request: WebRequest) : ResponseEntity<Any> {
        logger.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE)
                .body(ex.message)
    }


    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest) : ResponseEntity<Any> {
        logger.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.message)

    }

}