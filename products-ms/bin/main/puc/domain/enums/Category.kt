package puc.domain.enums

enum class Category(val id: Int, val description: String) {
    UNKNOWN(0, "Unknown category"),
    ELECTRONICS(1, "Devices and gadgets"),
    CLOTHING(2, "Apparel and accessories"),
    HOME_APPLIANCES(3, "Household appliances and tools"),
    BOOKS(4, "Printed or digital literature"),
    TOYS(5, "Games and toys for children"),
    FURNITURE(6, "Furniture and home decor"),
    BEAUTY(7, "Beauty and personal care products"),
    SPORTS(8, "Sports equipment and apparel"),
    GROCERIES(9, "Food and household goods"),
    AUTOMOTIVE(10, "Automobile parts and accessories");

    companion object {
        fun fromValue(value: String): Category {
            return values().find { it.description == value } ?: UNKNOWN
        }
    }
}