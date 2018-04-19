package com.rjs.myshows.server.controller.rest

import com.rjs.myshows.server.controller.UserController
import com.rjs.myshows.server.domain.dto.UserDto
import com.rjs.myshows.server.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ws/users")
class UserRestController(
    userService: UserService,
    modelMapper: ModelMapper
): UserController(userService, modelMapper) {
    @PostMapping("/login")
    fun login(@RequestBody userDto: UserDto): UserDto {
        val user = userService.findByUsernameAndPassword(userDto.username, userDto.password) ?:
            throw WebApplicationException(HttpStatus.UNAUTHORIZED, "User login failed.")

        return convertToUserDto(user)
    }

    @PostMapping("/create")
    fun createUser(@RequestBody userDto: UserDto): UserDto {
        if (userDto.password != userDto.confirmPassword) {
            throw WebApplicationException(HttpStatus.PRECONDITION_FAILED, "Passwords do not match.")
        }

        if (userService.findByUsername(userDto.username) != null) {
            throw WebApplicationException(HttpStatus.CONFLICT, "User with name ${userDto.username} already exists.")
        }

        val user = userService.createUser(userDto) ?: throw WebApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Error occurred while crating user ${userDto.username}")

        return convertToUserDto(user)
    }

    @PostMapping("/password-change")
    fun changePassword(@RequestBody userDto: UserDto): UserDto {
        var user = userService.findByUsername(userDto.username) ?: throw WebApplicationException(HttpStatus.NOT_FOUND, "User not found.")

        user.password = userDto.password
        user = userService.save(user)

        return convertToUserDto(user)
    }

    @PostMapping("/update")
    fun saveUser(@RequestBody userDto: UserDto): UserDto {
        var user = userService.findByUsername(userDto.username) ?: throw WebApplicationException(HttpStatus.NOT_FOUND, "User not found.")

        // This should apply only the allowable changes
        convertToUser(userDto, user)

        user = userService.save(user)

        return convertToUserDto(user)
    }
}