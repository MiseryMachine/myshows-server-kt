package com.rjs.myshows.server.service

import com.rjs.myshows.server.domain.ShowType
import com.rjs.myshows.server.repository.ShowTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("showTypeService")
@Transactional
class ShowTypeService(private val showTypeRepository: ShowTypeRepository): BaseService<ShowType, ShowTypeRepository>(showTypeRepository) {
    fun findByName(name: String) = showTypeRepository.findByName(name)
}