package puc.stock.excecoes


import java.lang.RuntimeException

class StockNotFoundException(message: String) : RuntimeException(message)

