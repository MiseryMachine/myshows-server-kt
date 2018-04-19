package com.rjs.myshows.server.service

import com.rjs.myshows.server.domain.UserShowFilter
import com.rjs.myshows.server.repository.UserShowFilterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("userShowFilterService")
@Transactional
class UserShowFilterService(val userShowFilterRepository: UserShowFilterRepository
): BaseService<UserShowFilter, UserShowFilterRepository>(userShowFilterRepository) {
    fun findByUserId(userId: Long) = userShowFilterRepository.findByUserId(userId)
    fun findByUsername(username: String) = userShowFilterRepository.findByUserUsername(username)

    fun deleteByFilterName(username: String, filterName: String) = userShowFilterRepository.deleteByUserUsernameAndName(username, filterName)
}