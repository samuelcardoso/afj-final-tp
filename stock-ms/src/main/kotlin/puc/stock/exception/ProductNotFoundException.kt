package puc.stock.exception

class ProductNotFoundException(
    override val message: String
) : RuntimeException(message)