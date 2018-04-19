package com.rjs.myshows.server.service.mdb.tmdb

class TmdbUrl(var url: String = "") {
    fun addPath(path: String): TmdbUrl {
        url += path

        return this
    }
}