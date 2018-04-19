package com.rjs.myshows.server.service.mdb.tmdb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("tmdb.api")
class TmdbConfig {
    var key: String = ""
    var locale: String = ""
    var url: String = ""
    var imageUrl: String = ""
    var imageNormalPath: String = ""
    var imageThumbPath: String = ""
    var genrePath: String = ""
    var moviePath: String = ""
    var tvPath: String = ""
    var searchPath: String = ""
    var listPath: String = ""

    fun getShowTypePath(showTypeName: String): String {
        return if ("TV".equals(showTypeName, false)) tvPath else moviePath
    }
}