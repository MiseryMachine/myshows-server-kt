package com.rjs.myshows.server.repository.filter

open class AtomicDataFilter(field: String, operator: String, value: Any?): DataFilter(field, operator, value) {
    constructor(field: String, operator: String): this(field, operator, null)
}