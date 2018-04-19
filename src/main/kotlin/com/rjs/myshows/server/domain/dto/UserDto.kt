package com.rjs.myshows.server.domain.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class UserDto(
    @NotNull(message = "Username is required.")
    @NotEmpty(message = "Username is required.")
    val username: String = "",

    @NotNull(message = "Email is required.")
    @NotEmpty(message = "Email is required.")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", message = "Invalid email address.")
    val email: String = "",

    @NotNull(message = "First name is required.")
    @NotEmpty(message = "First name is required.")
    val firstName: String = "",

    @NotNull(message = "Last name is required.")
    @NotEmpty(message = "Last name is required.")
    val lastName: String = "",

    @NotNull(message = "Password is required.")
    @NotEmpty(message = "Password is required.")
    val password: String = "",

    val confirmPassword: String = "",
    val newUser: Boolean = false,
    val roles: Set<String> = emptySet()
)