package com.rjs.myshows.server.service

import com.rjs.myshows.server.domain.BaseEntity
import com.rjs.myshows.server.repository.BaseRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional

@Transactional
class BaseService<E: BaseEntity, out R: BaseRepository<E>>(val repository: R) {
    fun getAll(): List<E> = repository.findAll()
    fun getAll(sort: Sort): List<E> = repository.findAll(sort)

    fun getMatching(example: E): List<E> = repository.findAll(Example.of(example))
    fun getMatching(example: E, sort: Sort): List<E> = repository.findAll(Example.of(example), sort)

    fun findById(id: Long): E? = repository.getOne(id)
    fun findByIds(ids: Collection<Long>): List<E> = repository.findAllById(ids)

    @Transactional
    fun save(entity: E): E = repository.save(entity)
    @Transactional
    fun saveAll(entities: List<E>): List<E> = repository.saveAll(entities)

    @Transactional
    fun delete(id: Long) = repository.deleteById(id)
    @Transactional
    fun delete(entity: E) = repository.delete(entity)
    @Transactional
    fun deleteAll(entities: List<E>) = repository.deleteAll(entities)
    @Transactional
    fun deleteAll() = repository.deleteAll()
}