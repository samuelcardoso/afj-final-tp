package puc.products.domain

data class PaginatedResponse<T>(
    val data: List<T>,
    val meta: PaginationMeta
)