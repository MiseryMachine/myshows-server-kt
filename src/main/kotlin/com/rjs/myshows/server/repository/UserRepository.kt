package com.rjs.myshows.server.repository

import com.rjs.myshows.server.domain.User

interface UserRepository: BaseRepository<User> {
    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    fun findByUsernameAndPassword(username: String, password: String): User?
}