package puc.model.enum

enum class Role(val permissions: Set<Permission>) {
    ROLE_USER(setOf(Permission.MAKE_PURCHASE)),
    ROLE_ADMIN(setOf(Permission.MAKE_PURCHASE, Permission.MANAGE_USERS));

    enum class Permission {
        MAKE_PURCHASE,
        MANAGE_USERS
    }
}
