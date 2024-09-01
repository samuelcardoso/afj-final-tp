package puc.application.dtos

data class FilterProductParamsDTO(
    val name:String?,
    val category:String?,
    val page:Int,
    val pageSize:Int,

)  {

    constructor(name: String?, category: String?, page: Int?, pageSize: Int?): this(
        name = name,
        category = category,
        page = page ?: 1,
        pageSize = pageSize ?: 30
        ) {
    }

    fun validate(totalPages: Int) {
                require(page >= 1) { "Page number cannot be less than 1." }

                if (totalPages  == 0) return
                require(page  <= totalPages) { "Requested page does not exist. Last available page: $totalPages" }
    }
}

