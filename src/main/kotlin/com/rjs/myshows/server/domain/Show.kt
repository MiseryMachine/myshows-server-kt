package com.rjs.myshows.server.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
class Show(
    id: Long? = null,

    @Column(name = "mdb_id")
    var mdbId: String = "",

    @Column(name = "imdb_id")
    var imdbId: String = "",

    var title: String = "",

    @Column(name = "show_rating", length = 10, nullable = false)
    var showRating: String = "N/R",

    var contents: String = "",

    @Transient
    var contentsArray: Array<String> = emptyArray(),

    @Column(name = "tag_line", length = 511)
    var tagLine: String = "",

    @Column(length = 2000)
    var description: String = "",

    @Column(name = "release_date")
    var releaseDate: LocalDate? = null,

    @Transient
    var releaseDateText: String = "",

    var runtime: Int = 0,

    @Column(name = "show_type", length = 40, nullable = false)
    var showType: String = "",

    @ElementCollection
    @CollectionTable(name = "show_genre", joinColumns = [JoinColumn(name = "show_id")])
    @Column(name = "genre", length = 40, nullable = false)
    @OrderBy(value = "genre")
    var genres: MutableSet<String> = linkedSetOf(),

    @Transient
    var genreText: String = "",

    @Column(name = "media_format")
    var mediaFormat: String = "",

    @Column(name = "my_notes", length = 2000)
    var myNotes: String = "",

    @Column(name = "star_rating")
    var starRating: Int = 0
) : BaseEntity(id)