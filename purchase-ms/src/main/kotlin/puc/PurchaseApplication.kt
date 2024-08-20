package puc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PurchaseApplication {}

fun main(args: Array<String>) {
	runApplication<PurchaseApplication>(*args)
}
