package com.rjs.myshows.server.repository.filter

import org.apache.commons.lang3.StringUtils

abstract class DataFilter(val field: String, val operator: String, val value: Any?) {
    protected open fun validate() {
        if (StringUtils.isEmpty(field)) {
            throw IllegalArgumentException("Field must be provided.")
        }

        if (StringUtils.isEmpty(operator)) {
            throw IllegalArgumentException("Operator must be provided.")
        }
    }
}