package com.rjs.myshows.server.domain.dto

data class ShowFilterDto(
    var showTypeName: String = "",
    var title: String = "",
    var starRating: Int = 0,
    var format: String = "",
    var genres: MutableSet<String> = hashSetOf()
)