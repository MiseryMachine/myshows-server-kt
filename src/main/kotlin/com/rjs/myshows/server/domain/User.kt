package com.rjs.myshows.server.domain

import com.rjs.myshows.server.domain.security.Role
import javax.persistence.*

@Entity
class User(
    id: Long? = null,

    @Column(length = 40, nullable = false, unique = true)
    var username: String = "",

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(name = "first_name", length = 40)
    var firstName: String = "",

    @Column(name = "last_name", length = 40)
    var lastName: String = "",

    var password: String = "",

    var enabled: Boolean = true,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = [(JoinColumn(name = "user_id"))])
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var roles: MutableSet<Role> = linkedSetOf()
): BaseEntity(id)