package com.rjs.myshows.server.repository

import com.rjs.myshows.server.domain.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BaseRepository<E: BaseEntity>: JpaRepository<E, Long>