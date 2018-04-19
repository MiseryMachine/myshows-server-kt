package com.rjs.myshows.server.util.web

import org.apache.commons.lang3.StringUtils
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.nio.charset.Charset

class HttpUtil {
/*
    fun <E> createHttpEntity(body: E?): HttpEntity<E> = createHttpEntity(body, null, null, null)

    fun <E> createHttpEntity(body: E?, username: String, password: String): HttpEntity<E> = createHttpEntity(body, username, password, null)

    fun <E> createHttpEntity(body: E?, contentType: MediaType): HttpEntity<E> = createHttpEntity(body, null, null, contentType)
*/

    fun <E> createHttpEntity(body: E? = null, username: String? = "", password: String? = "", contentType: MediaType? = MediaType.APPLICATION_JSON): HttpEntity<E> {
        val headers = HttpHeaders()

        if (null != contentType) {
            headers.contentType = contentType
        }

        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            val auth = "$username:$password"
            val encodedAuth = Base64.encodeBase64(auth.toByteArray(Charset.forName("US-ASCII")))
            headers.set("Authorization", "Basic ${String(encodedAuth)}")
        }

        return if (body != null) HttpEntity(body, headers) else HttpEntity(headers)
    }
}