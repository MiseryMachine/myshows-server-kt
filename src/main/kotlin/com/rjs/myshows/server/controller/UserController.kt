package com.rjs.myshows.server.controller

import com.rjs.myshows.server.domain.User
import com.rjs.myshows.server.domain.dto.UserDto
import com.rjs.myshows.server.service.UserService
import org.modelmapper.ModelMapper

abstract class UserController(
    protected val userService: UserService,
    protected val modelMapper: ModelMapper
) {
    protected fun convertToUser(userDto: UserDto, user: User) = modelMapper.map(userDto, user)
    protected fun convertToUserDto(user: User): UserDto = modelMapper.map(user, UserDto::class.java)
}
