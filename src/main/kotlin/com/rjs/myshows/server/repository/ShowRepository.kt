package com.rjs.myshows.server.repository

import com.rjs.myshows.server.domain.Show
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ShowRepository: BaseRepository<Show>, JpaSpecificationExecutor<Show> {
    fun findByMdbId(mdbId: String): Show?
    fun findByTitle(title: String): MutableList<Show>
}