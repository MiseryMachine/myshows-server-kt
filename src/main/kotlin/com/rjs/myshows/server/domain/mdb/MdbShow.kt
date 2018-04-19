package com.rjs.myshows.server.domain.mdb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
open class MdbShow(
    var id: Int = -1,

    var title: String = "",

    var overview: String = "",

    @JsonProperty("poster_path")
    var posterPath: String = "",

    @JsonProperty("release_date")
    var releaseDate: String = "",

    @JsonProperty("genre_ids")
    var genreIds: MutableList<Int> = mutableListOf()
)