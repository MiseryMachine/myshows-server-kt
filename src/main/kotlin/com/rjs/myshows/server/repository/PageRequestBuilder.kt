package com.rjs.myshows.server.repository

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageRequestBuilder(val field: String) {
    var direction = Sort.DEFAULT_DIRECTION
    var pageNumber = 0
    var pageSize = 10

    fun sortDirection(direction: Sort.Direction): PageRequestBuilder {
        this.direction = direction

        return this
    }

    fun pageNumber(pageNumber: Int): PageRequestBuilder {
        this.pageNumber = pageNumber

        return this
    }

    fun pageSize(pageSize: Int): PageRequestBuilder {
        this.pageSize = pageSize

        return this
    }

    fun getSort() = Sort(direction, field)

    fun pageRequest() = PageRequest.of(pageNumber, pageSize, getSort())
}