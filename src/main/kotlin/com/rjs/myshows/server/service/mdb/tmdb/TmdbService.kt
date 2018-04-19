package com.rjs.myshows.server.service.mdb.tmdb

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.rjs.myshows.server.config.AppConfig
import com.rjs.myshows.server.domain.Show
import com.rjs.myshows.server.domain.ShowType
import com.rjs.myshows.server.domain.mdb.MdbGenre
import com.rjs.myshows.server.domain.mdb.MdbShow
import com.rjs.myshows.server.domain.mdb.MdbShowDetail
import com.rjs.myshows.server.domain.mdb.MdbShowListing
import com.rjs.myshows.server.service.ShowService
import com.rjs.myshows.server.service.ShowTypeService
import com.rjs.myshows.server.service.mdb.MdbService
import com.rjs.myshows.server.util.saveImage
import com.rjs.myshows.server.util.web.RestClient
import com.rjs.myshows.server.util.web.WebServiceException
import org.apache.commons.lang3.StringUtils
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors

@Service("tmdbService")
class TmdbService(
    var appConfig: AppConfig,
    var tmdbConfig: TmdbConfig,
    var dateFormat: DateTimeFormatter,
    var jsonObjectMapper: ObjectMapper,
    var showService: ShowService,
    var showTypeService: ShowTypeService
): MdbService {
    private val logger = Logger.getLogger(TmdbService::class.java.name)
    private val restClient = RestClient()

    override fun searchShows(showTypeName: String, title: String): MutableList<MdbShow> {
        val results: MutableList<MdbShow> = mutableListOf()
        val urlParamsList: MutableList<String> = mutableListOf(
            "api_key=${tmdbConfig.key}",
            "query=$title",
            "language=${tmdbConfig.locale}"
        )

        try {// Build the full service URL
            val url = TmdbUrl(tmdbConfig.url).addPath(tmdbConfig.searchPath).addPath(tmdbConfig.getShowTypePath(showTypeName)).url + "?" +
                urlParamsList.stream().collect(Collectors.joining("&"))
            val showListing = consumeService<MdbShowListing>(url) ?: MdbShowListing()

            results.addAll(showListing.results)
            results.forEach{e ->
                e.posterPath = TmdbUrl(tmdbConfig.imageUrl).addPath(tmdbConfig.imageThumbPath).url + e.posterPath

                if (StringUtils.isEmpty(e.releaseDate)) {
                    e.releaseDate = "Unknown"
                }
            }
        }
        catch (e: IOException) {
            logger.log(Level.SEVERE, "Error searching shows", e)
        }

        return results
    }

    override fun addShow(showTypeName: String, mdbId: String): Show? {
        try {
            val show = showService.findByMdbId(mdbId)

            if (show != null) {
                return show
            }

            val urlParamsList: MutableList<String> = mutableListOf(
                "api_key=${tmdbConfig.key}",
                "language=${tmdbConfig.locale}"
            )
            val url = TmdbUrl(tmdbConfig.url)
                .addPath(tmdbConfig.getShowTypePath(showTypeName))
                .addPath("/$mdbId")
                .url +
                "?${urlParamsList.stream().collect(Collectors.joining("&"))}"
            val tmdbShow = consumeService<MdbShowDetail>(url) ?: MdbShowDetail()

            if (tmdbShow.id > -1) {
                val genres = convertGenres(tmdbShow.genres, showTypeName)
                var newShow = Show(null, tmdbShow.id.toString(), tmdbShow.imdbId, tmdbShow.title, "", "",
                    emptyArray(), tmdbShow.tagline, tmdbShow.overview, null, "",
                    tmdbShow.runtime, showTypeName, genres)

                if (StringUtils.isNotBlank(tmdbShow.releaseDate)) {
                    newShow.releaseDate = LocalDate.parse(tmdbShow.releaseDate, dateFormat)
                }

                newShow = showService.save(newShow)

                downloadPosterImage(newShow, tmdbShow.posterPath)

                return newShow
            }
        }
        catch (e: IOException) {
            logger.log(Level.SEVERE, "Error adding a show", e)
        }

        return null
    }

    override fun getGenres(showTypeName: String): MutableSet<String> {
        val url = TmdbUrl(tmdbConfig.url)
            .addPath(tmdbConfig.genrePath)
            .addPath(tmdbConfig.getShowTypePath(showTypeName))
            .addPath(tmdbConfig.listPath)
            .url

        return getGenres(url, showTypeName)
    }

    private fun getGenres(serviceUrl: String, showTypeName: String): MutableSet<String>  = convertGenres(getTmdbGenreMap(serviceUrl).values, showTypeName)

    private fun getTmdbGenreMap(serviceUrl: String): Map<String, MdbGenre> {
        val results: MutableMap<String, MdbGenre> = mutableMapOf()
        try {
            val urlParamsList = listOf(
                "api_key=${tmdbConfig.key}",
                "language=${tmdbConfig.locale}"
            )
            val url = "$serviceUrl?${urlParamsList.stream().collect(Collectors.joining("&"))}"
            val rootMap = consumeService<LinkedHashMap<String, Any>>(url) ?: linkedMapOf()
            val genreJson = jsonObjectMapper.writeValueAsString(rootMap["genres"])
            val mdbGenres: List<MdbGenre> = jsonObjectMapper.readValue(genreJson, object: TypeReference<List<MdbGenre>>(){})

            mdbGenres.forEach{g -> results[g.id.toString()] = g }
        }
        catch (e: IOException) {
            logger.log(Level.SEVERE, "Error retrieving genres", e)
        }

        return results
    }

    private fun downloadPosterImage(show: Show, tmdbPosterPath: String) {
        if (show.id == null) {
            throw IllegalStateException("Show must be saved prior to adding poster image.")
        }

        val localImagePath = appConfig.localImagePath(show.id.toString())
        var posterUrl = URL(tmdbConfig.imageUrl + tmdbConfig.imageNormalPath + tmdbPosterPath)
        saveImage(posterUrl, localImagePath, "poster")

        posterUrl = URL(tmdbConfig.imageUrl + tmdbConfig.imageThumbPath + tmdbPosterPath)
        saveImage(posterUrl, localImagePath, "poster_thumb")
    }

    private fun convertGenres(mdbGenres: Collection<MdbGenre>? = listOf(), showTypeName: String): MutableSet<String> {
        var showType = showTypeService.findByName(showTypeName) ?: ShowType(null, showTypeName)

        if (showType.id == null) {
            showType = showTypeService.save(showType)
        }

        val curGenres = showType.genres
        val initialSize = curGenres.size
        var genres:MutableSet<String> = linkedSetOf()

        if (mdbGenres!!.isEmpty()) {
            genres = mdbGenres.stream().map { g -> g.name }.collect(Collectors.toSet())
            curGenres.addAll(genres)

            if (initialSize != curGenres.size) {
                showTypeService.save(showType)
            }
        }

        return genres
    }

    private fun <E> consumeService(url: String, method: HttpMethod = HttpMethod.GET): E? {
        val responseEntity = restClient.exchange(method, url, "", "", "",
            object: ParameterizedTypeReference<E>(){})

        if (responseEntity.statusCode != HttpStatus.OK) {
            throw WebServiceException(responseEntity.statusCode,
                "Response status code = ${responseEntity.statusCode.reasonPhrase}")
        }

        return responseEntity.body
    }
}