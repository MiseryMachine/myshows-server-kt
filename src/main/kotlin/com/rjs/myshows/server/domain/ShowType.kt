package com.rjs.myshows.server.domain

import javax.persistence.*

@Entity
@Table(name = "show_type")
class ShowType(
    id: Long? = null,

    @Column(unique = true, nullable = false, length = 20)
    var name: String = "",

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "show_type_genre", joinColumns = [JoinColumn(name = "show_type_id")])
    @Column(name = "genre", length = 40, nullable = false)
    @OrderBy(value = "genre")
    var genres: MutableSet<String> = linkedSetOf(),

    @Transient
    var ratings: MutableSet<String> = linkedSetOf()
): BaseEntity(id)