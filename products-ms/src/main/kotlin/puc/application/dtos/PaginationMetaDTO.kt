package puc.application.dtos

data class PaginationMetaDTO (
    val total: Long,
    val perPage: Int,
    val currentPage: Int,
    val lastPage: Int
)