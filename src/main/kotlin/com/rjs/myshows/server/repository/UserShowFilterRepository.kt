package com.rjs.myshows.server.repository

import com.rjs.myshows.server.domain.UserShowFilter

interface UserShowFilterRepository: BaseRepository<UserShowFilter> {
    fun findByUserId(id: Long): List<UserShowFilter>
    fun findByUserUsername(username: String): List<UserShowFilter>

    fun deleteByUserUsernameAndName(username: String, filterName: String)
}