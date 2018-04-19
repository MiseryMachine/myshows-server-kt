package com.rjs.myshows.server.domain

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null
) {
    fun uuid() = this.javaClass.name + ":" + id

    override fun toString(): String {
        return uuid()
    }
}