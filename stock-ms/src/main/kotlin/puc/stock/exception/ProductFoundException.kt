package puc.stock.exception

class ProductFoundException (
    override val message: String
) : RuntimeException(message)