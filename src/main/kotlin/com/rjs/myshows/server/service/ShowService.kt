package com.rjs.myshows.server.service

import com.rjs.myshows.server.config.AppConfig
import com.rjs.myshows.server.domain.Show
import com.rjs.myshows.server.domain.UserShowFilter
import com.rjs.myshows.server.domain.dto.ShowFilterDto
import com.rjs.myshows.server.repository.ShowRepository
import com.rjs.myshows.server.repository.filter.*
import com.rjs.myshows.server.repository.specification.DataSpecification
import com.rjs.myshows.server.util.createThumbImage
import com.rjs.myshows.server.util.jpegImage
import com.rjs.myshows.server.util.pngImage
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.imageio.ImageIO

@Service("showService")
@Transactional
class ShowService(
    private val appConfig: AppConfig,
    private val showRepository: ShowRepository,
    private val defaultBoxArt: File?,
    private val defaultBoxArtThumb: File?
): BaseService<Show, ShowRepository>(showRepository) {
    private val defaultSort = Sort.by(
        Sort.Order(Sort.Direction.DESC, "starRating"),
        Sort.Order(Sort.Direction.ASC, "title")
    )

    fun findByMdbId(mdbId: String): Show? = showRepository.findByMdbId(mdbId)
    fun findByTitleExact(title: String): MutableList<Show> = showRepository.findByTitle(title)

    fun searchShows(showFilter: ShowFilterDto): List<Show> {
        val paramMap: MutableMap<String, Any> = hashMapOf()
//        paramMap["showTypeName"] = showFilter.showTypeName
        paramMap["title"] = showFilter.title
        paramMap["starRating"] = showFilter.starRating
        paramMap["format"] = showFilter.format
        paramMap["genres"] = showFilter.genres

        return searchShows(showFilter.showTypeName, paramMap, defaultSort)
    }

    fun searchShows(showTypeName: String, filter: Map<String, Any?>?): List<Show> = searchShows(showTypeName, filter, null)

    fun searchShows(showTypeName: String, filter: Map<String, Any?>?, sort: Sort?): List<Show> {
        if (StringUtils.isNoneBlank(showTypeName)) {
            val spec: Specification<Show>? = buildShowSpecification(showTypeName, filter)

            if (spec != null) {
                return if (sort != null) showRepository.findAll(spec, sort) else showRepository.findAll(spec)
            }
        }

        return emptyList()
    }

    fun getShowDto(userShowFilter: UserShowFilter): ShowFilterDto {
        return ShowFilterDto(
            userShowFilter.showTypeName,
            userShowFilter.title,
            userShowFilter.starRating,
            userShowFilter.format,
            userShowFilter.genres
        )
    }

    fun getShowPosterData(showId: Long?, thumb: Boolean): ByteArray {
        if (showId == null) {
            return ByteArray(0)
        }

        val localPosterPath = appConfig.localImagePath(showId.toString())
        var ext = ".png"
        var posterFile = File(localPosterPath, "poster$ext")
        var imageType = pngImage

        if (!posterFile.exists()) {
            ext = ".jpg"
            posterFile = File(localPosterPath, "poster$ext")
            imageType = jpegImage
        }

        if (!posterFile.exists()) {
            ext = ".jpeg"
            posterFile = File(localPosterPath, "poster$ext")
            imageType = jpegImage
        }

        var boxArtFile: File? = null

        if (thumb) {
            if (posterFile.exists()) {
                boxArtFile = File(localPosterPath, "poster_thumb$ext")

                if (!boxArtFile.exists()) {
                    val thumbImage = createThumbImage(ImageIO.read(posterFile))
                    ImageIO.write(thumbImage, imageType, FileOutputStream(boxArtFile))
                }
            }
            else if (defaultBoxArtThumb != null) {
                boxArtFile = defaultBoxArtThumb
            }
        }
        else {
            if (posterFile.exists()) {
                boxArtFile = posterFile
            }
            else if (defaultBoxArt != null) {
                boxArtFile = defaultBoxArt
            }
        }

        if (boxArtFile != null) {
            val fis = FileInputStream(boxArtFile)

            try {
                return IOUtils.toByteArray(fis)
            }
            catch (e: Exception) {
            }
            finally {
                fis.close()
            }
        }

        return ByteArray(0)
    }

    private fun buildShowSpecification(showTypeName: String, params: Map<String, Any?>?): Specification<Show>? {
        if (StringUtils.isBlank(showTypeName)) {
            return null
        }

        var spec: Specification<Show> = DataSpecification(AtomicDataFilter("showType", eqOperator, showTypeName))

        if (params != null && params.isNotEmpty()) {
            val paramNames: Set<String> = params.keys

            for (paramName in paramNames) {
                when (paramName) {
                    "title" -> {
                        val titleValue: String? = params[paramName] as String

                        if (StringUtils.isNotEmpty(titleValue)) {
                            spec = Specification.where(spec).and(DataSpecification(StringDataFilter("title", likeOperator, titleValue)))
                        }
                    }

                    "genres" -> {
                        val genreValues: Set<String>? = params.get(paramName) as Set<String>

                        if (genreValues != null && genreValues.isNotEmpty()) {
                            spec = Specification.where(spec).and(DataSpecification(CollectionDataFilter("genres", containsOperator, genreValues)))
                        }
                    }

                    "mediaFormat" -> {
                        val formatValue: String? = params[paramName] as String

                        if (StringUtils.isNotEmpty(formatValue) && !formatValue.equals("any", true)) {
                            spec = Specification.where(spec).and(DataSpecification(StringDataFilter("mediaFormat", eqOperator, formatValue)))
                        }
                    }

                    "starRating" -> {
                        val ratingValue: Int? = params[paramName] as Int

                        if (ratingValue != null) {
                            spec = Specification.where(spec).and(DataSpecification(StringDataFilter("starRating", gteOperator, ratingValue)))
                        }
                    }
                }
            }
        }

        return spec
    }
}