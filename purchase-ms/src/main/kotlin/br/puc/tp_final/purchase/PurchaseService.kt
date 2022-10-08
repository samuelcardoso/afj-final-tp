package br.puc.tp_final.purchase

import org.springframework.stereotype.Service
import java.util.*

@Service
class PurchaseService {
    fun buy(): UUID = UUID.randomUUID()
}