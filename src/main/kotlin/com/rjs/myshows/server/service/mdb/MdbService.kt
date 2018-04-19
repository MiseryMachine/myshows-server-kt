package com.rjs.myshows.server.service.mdb

import com.rjs.myshows.server.domain.Show
import com.rjs.myshows.server.domain.mdb.MdbShow

interface MdbService {
    fun searchShows(showTypeName: String, title: String): MutableList<MdbShow>

    fun addShow(showTypeName: String, mdbId: String): Show?

    fun getGenres(showTypeName: String): MutableSet<String>
}