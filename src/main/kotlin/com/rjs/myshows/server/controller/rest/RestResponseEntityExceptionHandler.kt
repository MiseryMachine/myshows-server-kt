package com.rjs.myshows.server.controller.rest

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [WebApplicationException::class])
    fun handleWebApplicationException(e: WebApplicationException, request: WebRequest) =
        handleExceptionInternal(e, e.message, HttpHeaders(), e.status, request)
}