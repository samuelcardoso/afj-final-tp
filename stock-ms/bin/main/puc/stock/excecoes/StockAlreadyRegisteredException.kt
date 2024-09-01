package puc.stock.excecoes

import java.lang.RuntimeException

class StockAlreadyRegisteredException(message: String) : RuntimeException(message)