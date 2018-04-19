package com.rjs.myshows.server.domain.mdb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class MdbShowDetail(
    id: Int = -1,

    title: String = "",

    overview: String = "",

    @JsonProperty("poster_path")
    posterPath: String = "",

    @JsonProperty("release_date")
    releaseDate: String = "",

    @JsonProperty("genre_ids")
    genreIds: MutableList<Int> = mutableListOf(),

    var genres: MutableList<MdbGenre> = mutableListOf(),

    @JsonProperty("imdb_id")
    var imdbId: String = "",

    var runtime: Int = -1,

    var tagline: String = ""
): MdbShow(id, title, overview, posterPath, releaseDate, genreIds)