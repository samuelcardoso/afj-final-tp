package puc.stock.excecoes

import java.lang.RuntimeException

class InsufficientStockException(message: String) : RuntimeException(message)
