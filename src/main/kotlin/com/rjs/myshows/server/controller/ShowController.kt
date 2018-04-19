package com.rjs.myshows.server.controller

import com.rjs.myshows.server.config.AppConfig
import com.rjs.myshows.server.domain.Show
import com.rjs.myshows.server.domain.UserShowFilter
import com.rjs.myshows.server.domain.dto.ShowDto
import com.rjs.myshows.server.domain.dto.ShowFilterDto
import com.rjs.myshows.server.domain.mediaFormats
import com.rjs.myshows.server.domain.starRatings
import com.rjs.myshows.server.service.ShowService
import com.rjs.myshows.server.service.ShowTypeService
import org.modelmapper.ModelMapper
import java.util.stream.Collectors

open class ShowController(
    protected val showService: ShowService,
    protected val showTypeService: ShowTypeService,
    protected val modelMapper: ModelMapper,
    protected val appConfig: AppConfig
) {

    protected fun initializeShowFilter() = ShowFilterDto(
        "Movie",
        mediaFormats[0],
        starRatings[starRatings.lastIndex]
    )

    protected fun buildSearchModel(
        showFilterDto: ShowFilterDto,
        userShowFilters: MutableList<UserShowFilter>,
        selUserFilter: String? = null,
        searchResults: MutableList<Show> = mutableListOf())
        : Map<String, Any?> {
        val model: MutableMap<String, Any?> = hashMapOf()
        val showDtos: List<ShowDto> =
            if (searchResults.isNotEmpty()) {
                searchResults.stream().map {show: Show -> convertToShowDto(show)}.collect(Collectors.toList())
            }
            else {
                listOf()
            }

        model["showTypes"] = showTypeService.getAll()
        model["starRatings"] = starRatings
        model["mediaFormats"] = mediaFormats
        model["userFilters"] = userShowFilters
        model["selUserFilter"] = selUserFilter
        model["showSearchFilter"] = showFilterDto
        model["numResults"] = showDtos.size
        model["shows"] = showDtos

        return model
    }

    protected fun convertToShowDto(show: Show) = modelMapper.map(show, ShowDto::class.java)!!
}