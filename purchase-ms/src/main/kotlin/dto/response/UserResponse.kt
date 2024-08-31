package puc.dto.response

data class UserResponse(
    val username: String,
    val roles: Set<String>
){
    constructor() : this("", emptySet())
}
