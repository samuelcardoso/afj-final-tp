package puc.stock.controller.handler

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import puc.stock.exception.NotEnoughStockException
import puc.stock.exception.ProductFoundException
import puc.stock.exception.ProductNotFoundException

@ControllerAdvice
class ValidationExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validationExceptionHandler(exception: MethodArgumentNotValidException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errors: List<FieldMessage> =
            exception.fieldErrors
                .map { fieldError -> FieldMessage(fieldError.field, fieldError.defaultMessage!!) }

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
    @ExceptionHandler(ProductFoundException::class)
    fun productFoundExceptionHandler(exception: ProductFoundException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.CONFLICT.value(),
                exception.message,
                HttpStatus.CONFLICT.name,
                request.requestURI
            ))
    }
}