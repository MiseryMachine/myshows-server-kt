package com.rjs.myshows.server.repository

import com.rjs.myshows.server.domain.ShowType

interface ShowTypeRepository: BaseRepository<ShowType> {
    fun findByName(name: String): ShowType?
}