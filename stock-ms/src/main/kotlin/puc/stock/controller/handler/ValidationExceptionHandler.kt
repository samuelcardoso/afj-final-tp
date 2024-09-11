package puc.stock.controller.handler

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import puc.stock.exception.NotEnoughStockException
import puc.stock.exception.ProductAlreadyExistsException
import puc.stock.exception.ProductNotFoundException

@ControllerAdvice
class ValidationExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validationExceptionHandler(exception: MethodArgumentNotValidException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errors: List<FieldMessage> =
            exception.fieldErrors
                .map { fieldError -> FieldMessage(fieldError.field, fieldError.defaultMessage!!) }

        this.loggerFactory(exception.javaClass.name,"Erro de validação de input", request.requestURI, request.method, HttpStatus.UNPROCESSABLE_ENTITY.value())
        return ResponseEntity.unprocessableEntity().body(
            ValidationErrorResponse(
                errors,
                System.currentTimeMillis(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Input validation error",
                HttpStatus.UNPROCESSABLE_ENTITY.name,
                request.requestURI
            )
        )
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException::class)
    fun productNotFoundExceptionHandler(exception: ProductNotFoundException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        this.loggerFactory(exception.javaClass.name, exception.message, request.requestURI, request.method, HttpStatus.NOT_FOUND.value())
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                exception.message,
                HttpStatus.NOT_FOUND.name,
                request.requestURI
            ))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEnoughStockException::class)
    fun notEnoughStockExceptionHandler(exception: NotEnoughStockException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        this.loggerFactory(exception.javaClass.name, exception.message, request.requestURI, request.method, HttpStatus.BAD_REQUEST.value())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                exception.message,
                HttpStatus.BAD_REQUEST.name,
                request.requestURI
            ))
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProductAlreadyExistsException::class)
    fun productAlreadyExistsExceptionHandler(exception: ProductAlreadyExistsException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        this.loggerFactory(exception.javaClass.name, exception.message, request.requestURI, request.method, HttpStatus.CONFLICT.value())
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.CONFLICT.value(),
                exception.message,
                HttpStatus.CONFLICT.name,
                request.requestURI
            ))
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    fun genericErrorExceptionHandler(exception: RuntimeException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        this.loggerFactory(exception.javaClass.name, exception.message, request.requestURI, request.method, HttpStatus.INTERNAL_SERVER_ERROR.value())
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.message,
                HttpStatus.INTERNAL_SERVER_ERROR.name,
                request.requestURI
            ))
    }

    fun loggerFactory(error: String?, message: String?, path: String, method: String, status: Int) {
        logger.error("==== Error: [{}]. Message: [{}] Path: [{}]. Method: [{}]. Code: [{}] ====", error, message, path, method, status)
    }
}