package puc.model.enum

enum class RoleEnum(val permissionEnums: Set<PermissionEnum>) {
    ROLE_USER(setOf(PermissionEnum.MAKE_PURCHASE)),
    ROLE_ADMIN(setOf(PermissionEnum.MAKE_PURCHASE, PermissionEnum.MANAGE_USERS));

    enum class PermissionEnum {
        MAKE_PURCHASE,
        MANAGE_USERS
    }
}
