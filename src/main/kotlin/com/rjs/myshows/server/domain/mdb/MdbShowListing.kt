package com.rjs.myshows.server.domain.mdb

class MdbShowListing(
    var page: Int? = 0,
    var results: MutableList<MdbShow> = mutableListOf()
)