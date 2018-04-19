package com.rjs.myshows.server.domain.security

enum class Role(val text: String) {
    ROLE_USER("User"),
    ROLE_REST_USER("REST User"),
    ROLE_ADMIN("Admin");

    fun findByText(textVal: String): Role? {
        for (role in Role.values()) {
            if (textVal == role.text) {
                return role
            }
        }

        return null
    }

    override fun toString() = text
}