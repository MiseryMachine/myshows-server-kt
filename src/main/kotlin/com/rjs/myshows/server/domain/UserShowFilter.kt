package com.rjs.myshows.server.domain

import javax.persistence.*


@Entity
@Table(name = "user_show_filter", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "name"])])
class UserShowFilter(
    id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    var user: User = User(),

    @Column(length = 40, nullable = false)
    var name: String = "",

    @Column(name = "show_type", length = 40, nullable = false)
    var showTypeName: String,

    var title: String = "",

    @Column(name = "star_rating")
    var starRating: Int = 0,

    @Column(name = "media_format")
    var format: String = "",

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_show_filter_genre", joinColumns = [(JoinColumn(name = "user_show_filter_id"))])
    @Column(name = "genre", length = 40, nullable = false)
    @OrderBy("genre")
    var genres: MutableSet<String> = hashSetOf()
): BaseEntity(id)