package puc.stock.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import puc.stock.model.Stock
import java.util.Optional

interface StockRepository : JpaRepository<Stock, Long> {
    //TODOS - mudei para fins didático (ambos fazem o mesmo)
    //fun findByProductId(productId: String): Stock?

    @Query(value = "SELECT * FROM stock where product_id like %?1%", nativeQuery = true)
    fun findByProductId(productId: String) : Optional<Stock?>


}
