package com.rjs.myshows.server.util

import org.apache.commons.lang3.StringUtils
import org.imgscalr.Scalr
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger
import javax.imageio.ImageIO

const val jpegImage = "JPEG"
const val pngImage = "PNG"

private val logger = Logger.getLogger("ImageUtil")
private const val thumbWidth = 92

fun createThumbImage(original: BufferedImage): BufferedImage {
    val ratio: Double = thumbWidth.toDouble() / original.width.toDouble()
    val thumbHeight: Double = ratio * original.height.toDouble()

    return Scalr.resize(original, Scalr.Method.ULTRA_QUALITY, thumbWidth, thumbHeight.toInt())
}

fun saveImage(imageUrl: URL, savePathStr: String?, imageName: String?) {
    if (StringUtils.isEmpty(savePathStr)) {
        logger.severe("Saving image error: savePathStr parameter is empty.")

        return
    }

    val savePath = File(savePathStr)

    if (!savePath.exists()) {
        if (savePath.mkdirs()) {
            logger.info("Created local image path: $savePathStr.")
        }
        else {
            logger.severe("Unable to create local image path: $savePathStr.")

            return
        }
    }

    if (!savePath.isDirectory) {
        logger.severe("Saving image error: $savePathStr is not a directory.")

        return
    }

    saveImage(imageUrl, savePath, imageName)
}

fun saveImage(imageUrl: URL, savePath: File, imageName: String?) {
    if (StringUtils.isBlank(imageName)) {
        logger.severe("Saving image error: imageName parameter is empty.")

        return
    }

    try {
        var imageFileSuffix = "$jpegImage"
        val connection = imageUrl.openConnection()
        val contentType = connection.contentType
        val readers = ImageIO.getImageReadersByMIMEType(contentType)
        var suffixFound = false

        while (!suffixFound && readers.hasNext()) {
            val provider = readers.next().originatingProvider

            if (provider != null ) {
                val suffixes = provider.fileSuffixes

                if (suffixes != null && suffixes.isNotEmpty()) {
                    imageFileSuffix = if (suffixes[0].startsWith(".")) suffixes[0].substring(1) else suffixes[0]
                    suffixFound = true
                }
            }
        }

        val posterImg = ImageIO.read(connection.getInputStream())
        ImageIO.write(posterImg, imageFileSuffix, FileOutputStream(File(savePath, "$imageName.$imageFileSuffix")))
    }
    catch (e: IOException) {
        logger.log(Level.SEVERE, "Error saving image from " + imageUrl.toString(), e)
    }
}