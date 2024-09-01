package puc.purchase.excecoes

import java.lang.RuntimeException

class InsufficientStockException(message: String) : RuntimeException(message)
