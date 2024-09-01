package puc.application.dtos

data class PaginatedResponseDTO<T>(
    val data: List<T>,
    val meta: PaginationMetaDTO
)