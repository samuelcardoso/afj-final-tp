package puc.stock.exception

class NotEnoughStockException(
    override val message: String
) : RuntimeException(message)