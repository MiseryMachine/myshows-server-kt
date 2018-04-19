package com.rjs.myshows.server.repository.filter

class CollectionDataFilter(field: String, operator: String, value: Any?): AtomicDataFilter(field, operator, value) {
    constructor(field: String, operator: String): this(field, operator, null)

    override fun validate() {
        super.validate()

        if (value == null) {
            var badOperator = ""

            when (operator) {
                inOperator, containsOperator -> badOperator = operator
            }

            if (badOperator != "") {
                throw IllegalArgumentException("Cannot use $badOperator operator with a null value.")
            }
        }
    }
}