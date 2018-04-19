package com.rjs.myshows.server.repository.specification

import com.rjs.myshows.server.repository.filter.*
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import javax.persistence.criteria.*

class DataSpecification<E>(private val dataFilter: DataFilter): Specification<E> {
    override fun toPredicate(root: Root<E>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        val fieldPath: Path<Any> = root.get(dataFilter.field)

        if (dataFilter.value != null) {
            when (dataFilter.operator) {
                eqOperator -> return criteriaBuilder.equal(fieldPath, dataFilter.value)
                neOperator -> return criteriaBuilder.notEqual(fieldPath, dataFilter.value)
            }

            val fieldClass = fieldPath.javaType

            if (fieldClass == String::class.java) {
                val strPath: Path<String> = fieldPath as Path<String>

                if (dataFilter.operator == likeOperator) {
                    return criteriaBuilder.like(strPath, "%" + dataFilter.value + "%")
                }
            }
            else if (fieldClass == Integer::class.java || fieldClass == Int::class.java) {
                val intPath: Path<Int> = fieldPath as Path<Int>

                when (dataFilter.operator) {
                    gtOperator -> return criteriaBuilder.greaterThan<Int>(intPath, dataFilter.value as Int)
                    gteOperator -> return criteriaBuilder.greaterThanOrEqualTo<Int>(intPath, dataFilter.value as Int)
                    ltOperator -> return criteriaBuilder.lessThan<Int>(intPath, dataFilter.value as Int)
                    lteOperator -> return criteriaBuilder.lessThanOrEqualTo<Int>(intPath, dataFilter.value as Int)
                }
            }
//            else if (fieldClass == Long::class.java || fieldClass == long::class.java) {
            else if (fieldClass == Long::class.java) {
                val longPath: Path<Long> = fieldPath as Path<Long>

                when (dataFilter.operator) {
                    gtOperator -> return criteriaBuilder.greaterThan<Long>(longPath, dataFilter.value as Long)
                    gteOperator -> return criteriaBuilder.greaterThanOrEqualTo<Long>(longPath, dataFilter.value as Long)
                    ltOperator -> return criteriaBuilder.lessThan<Long>(longPath, dataFilter.value as Long)
                    lteOperator -> return criteriaBuilder.lessThanOrEqualTo<Long>(longPath, dataFilter.value as Long)
                }
            }
//            else if (fieldClass == Float::class.java || fieldClass == float::class.java) {
            else if (fieldClass == Float::class.java) {
                val floatPath: Path<Float> = fieldPath as Path<Float>

                when (dataFilter.operator) {
                    gtOperator -> return criteriaBuilder.greaterThan<Float>(floatPath, dataFilter.value as Float)
                    gteOperator -> return criteriaBuilder.greaterThanOrEqualTo<Float>(floatPath, dataFilter.value as Float)
                    ltOperator -> return criteriaBuilder.lessThan<Float>(floatPath, dataFilter.value as Float)
                    lteOperator -> return criteriaBuilder.lessThanOrEqualTo<Float>(floatPath, dataFilter.value as Float)
                }
            }
//            else if (fieldClass == Double::class.java || fieldClass == double::class.java) {
            else if (fieldClass == Double::class.java) {
                val doublePath: Path<Double> = fieldPath as Path<Double>

                when (dataFilter.operator) {
                    gtOperator -> return criteriaBuilder.greaterThan<Double>(doublePath, dataFilter.value as Double)
                    gteOperator -> return criteriaBuilder.greaterThanOrEqualTo<Double>(doublePath, dataFilter.value as Double)
                    ltOperator -> return criteriaBuilder.lessThan<Double>(doublePath, dataFilter.value as Double)
                    lteOperator -> return criteriaBuilder.lessThanOrEqualTo<Double>(doublePath, dataFilter.value as Double)
                }
            }
            else if (fieldClass == LocalDate::class.java) {
                val datePath: Path<LocalDate> = fieldPath as Path<LocalDate>

                when (dataFilter.operator) {
                    gtOperator -> return criteriaBuilder.greaterThan<LocalDate>(datePath, dataFilter.value as LocalDate)
                    gteOperator -> return criteriaBuilder.greaterThanOrEqualTo<LocalDate>(datePath, dataFilter.value as LocalDate)
                    ltOperator -> return criteriaBuilder.lessThan<LocalDate>(datePath, dataFilter.value as LocalDate)
                    lteOperator -> return criteriaBuilder.lessThanOrEqualTo<LocalDate>(datePath, dataFilter.value as LocalDate)
                }
            }
            else if (fieldClass == Collection::class.java) {
                val coll:Collection<Any> = dataFilter.value as Collection<Any>

                when (dataFilter.operator) {
                    inOperator -> {
                        val inBuilder: CriteriaBuilder.In<Any> = criteriaBuilder.`in`(fieldPath)

                        for (item in coll) {
                            inBuilder.value(item)
                        }

                        return inBuilder
                    }
                    containsOperator -> {
                        if (!coll.isEmpty()) {
                            val predicates:Array<Predicate?> = arrayOfNulls(coll.size)
                            var idx = 0

                            for (value in coll) {
                                predicates[idx++] = criteriaBuilder.isMember(value, fieldPath as Path<Collection<Any>>)
                            }

                            return if (predicates.size > 1) criteriaBuilder.or(*predicates) else predicates[0]
                        }
                    }
                }
            }
        }
        else {
            when (dataFilter.operator) {
                eqOperator -> return criteriaBuilder.isNull(fieldPath)
                neOperator -> return criteriaBuilder.isNotNull(fieldPath)
            }
        }

        return null
    }
}