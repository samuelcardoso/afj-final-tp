package puc.products.domain

data class PaginationMeta (
    val total: Long,
    val perPage: Int,
    val currentPage: Int,
    val lastPage: Int
)