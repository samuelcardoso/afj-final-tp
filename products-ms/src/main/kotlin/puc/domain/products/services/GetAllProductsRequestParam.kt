package puc.domain.products.services

data class GetAllProductsRequestParam(
    val name:String?,
    val price:Double?,
    val description:String?,
    val weight:Double?,
    val measure:String?,
    val color:String?,
    val category:String?,
    val brand:String?,
)