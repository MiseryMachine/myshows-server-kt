package com.rjs.myshows.server.config

import com.rjs.myshows.server.domain.User
import com.rjs.myshows.server.domain.dto.UserDto
import org.modelmapper.ModelMapper
import org.modelmapper.PropertyMap
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger

@Configuration
@ConfigurationProperties("my-movies")
class AppConfig {
    val logger = Logger.getLogger(AppConfig::class.java.name)
    val datePattern = "yyyy-MM-dd"
    var localFilePath = ""

    @Bean
    fun dateFormat(): DateTimeFormatter = DateTimeFormatter.ofPattern(datePattern)

/*
    @Bean
    fun jsonObjectMapper(): ObjectMapper {
        val objectMapper = jacksonObjectMapper()
        objectMapper.dateFormat = dateFormat()

        return objectMapper
    }
*/

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        val propMap1 = object: PropertyMap<User, UserDto>(User::class.java, UserDto::class.java) {
            override fun configure() {
                skip().password
                skip().confirmPassword
            }
        }
        val propMap2 = object: PropertyMap<UserDto, User>(UserDto::class.java, User::class.java) {
            override fun configure() {
                skip().id
                skip().username
                skip().password
            }
        }

        modelMapper.addMappings(propMap1)
        modelMapper.addMappings(propMap2)

        return modelMapper
    }

    @Bean
    fun defaultBoxArt():File? {
        try {
            return ClassPathResource("/static/img/default-movie-box.png").file
        }
        catch (e: Exception) {
            logger.log(Level.SEVERE, "Cannot locate default box art image.", e)
        }

        return null
    }

    @Bean
    fun defaultBoxArtThumb():File? {
        try {
            return ClassPathResource("/static/img/default-movie-box_thumb.png").file
        }
        catch (e: Exception) {
            logger.log(Level.SEVERE, "Cannot locate default box art thumb image.", e)
        }

        return null
    }

    fun localImagePath(showId:String) = "$localFilePath/shows/$showId/images"
}