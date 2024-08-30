package puc.products.`in`.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDate

@RestControllerAdvice
class ProductsExceptionHandler: ResponseEntityExceptionHandler() {


    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ProblemDetail {
        val problemDetail: ProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.PRECONDITION_FAILED, e.localizedMessage)
        problemDetail.title = "Fill the fields correctly"
        problemDetail.detail = "${e.message}"
        problemDetail.setProperty("timestamp", LocalDate.now())
        return problemDetail
    }
}