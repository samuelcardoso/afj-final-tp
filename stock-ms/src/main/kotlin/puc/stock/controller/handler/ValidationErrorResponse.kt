package puc.stock.controller.handler

data class ValidationErrorResponse(
    var errors: List<FieldMessage>,
    override val timestamp: Long,
    override val status: Int,
    override val message: String,
    override val error: String,
    override val path: String
) : ErrorResponse(
    timestamp,
    status,
    message,
    error,
    path
) {
}
