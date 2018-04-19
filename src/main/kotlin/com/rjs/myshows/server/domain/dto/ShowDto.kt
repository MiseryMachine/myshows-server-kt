package com.rjs.myshows.server.domain.dto

data class ShowDto(
    var id: Long = -1,
    var title: String = "",
    var tagLine: String = "",
    var genreText: String = "",
    var mediaFormat: String = "",
    var starRating: Int = 0
)