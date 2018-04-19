package com.rjs.myshows.server.service

import com.rjs.myshows.server.config.security.WebSecurityConfig
import com.rjs.myshows.server.domain.User
import com.rjs.myshows.server.domain.dto.UserDto
import com.rjs.myshows.server.domain.security.Role
import com.rjs.myshows.server.repository.PageRequestBuilder
import com.rjs.myshows.server.repository.UserRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest

@Service("userService")
@Transactional
class UserService(
    val userRepository: UserRepository,
    val securityConfig: WebSecurityConfig
) : BaseService<User, UserRepository>(userRepository) {
    val defaultStringMatcher = ExampleMatcher.matching()
        .withMatcher("username", ExampleMatcher.GenericPropertyMatcher().ignoreCase())
        .withMatcher("username", ExampleMatcher.GenericPropertyMatcher().contains())

    // Gets
    fun getUsers(): List<User> = userRepository.findAll()
    fun getUsersPaged(exampleUser: User): Page<User> = getUsersPaged(exampleUser, 0, 10)
    fun getUsersPaged(exampleUser: User?, pageNumber: Int, pageSize: Int): Page<User> {
        val pageRequest = PageRequestBuilder("username").pageNumber(pageNumber).pageSize(pageSize).pageRequest()

        return if (exampleUser == null) userRepository.findAll(pageRequest)
        else userRepository.findAll(Example.of(exampleUser), pageRequest)
    }

    // Finds
    fun findByUsername(username: String): User? = userRepository.findByUsername(username)
    fun findByUsernameAndPassword(username: String,  password: String): User? = userRepository.findByUsernameAndPassword(username, password)
    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun userExists(username: String) = findByUsername(username) != null

//    fun login(username: String, password: String): User =
//        userRepository.findByUsernameAndPassword(username, password) ?: throw Exception("Invalid username or password.")

    fun hasRoles(request: HttpServletRequest, vararg roleNames: String): Boolean {
        for (roleName in roleNames) {
            if (request.isUserInRole(roleName)) {
                return true
            }
        }

        return false
    }

    // Creates/Saves
    @Transactional
    fun createUser(userDto: UserDto): User? {
        if (findByUsername(userDto.username) == null) {
            val user = User()
            user.username = userDto.username

            return saveUser(user, userDto)
        }

        return null
    }

    @Transactional
    fun updateUser(userDto: UserDto): User? {
        val user = findByUsername(userDto.username)

        return if (user != null) saveUser(user, userDto) else null
    }

    private fun saveUser(user: User, userDto: UserDto): User {
        user.firstName = userDto.firstName
        user.lastName = userDto.lastName
        user.email = userDto.email
        user.roles.add(Role.ROLE_USER)

        val pw = userDto.password

        if (StringUtils.isNotBlank(pw)) {
            user.password = securityConfig.encode(pw)
        }

        return save(user)
    }
}