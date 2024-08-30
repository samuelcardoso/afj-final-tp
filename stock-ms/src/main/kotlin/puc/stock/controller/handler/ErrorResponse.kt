package puc.stock.controller.handler

open class ErrorResponse(
    open val timestamp: Long,
    open val status: Int,
    open val message: String?,
    open val error: String,
    open val path: String
)
