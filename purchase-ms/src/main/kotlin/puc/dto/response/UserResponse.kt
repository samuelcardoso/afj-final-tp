package puc.dto.response

data class UserResponse(
    val id: Long?,
    val username: String,
    val roles: Set<String>
){
    constructor() : this(0L, "", emptySet())
}
