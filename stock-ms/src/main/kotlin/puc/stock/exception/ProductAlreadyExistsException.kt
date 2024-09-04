package puc.stock.exception

class ProductAlreadyExistsException (
    override val message: String
) : RuntimeException(message)