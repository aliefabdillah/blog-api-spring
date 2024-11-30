package com.zuraa.blog_api_spring.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.Date

@Service
class FileStorageService(
    @Value("\${file.upload-dir}") private val uploadDir: String
) {

    init {
        val uploadPath = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectory(uploadPath)
        }
    }

    fun storeFile(file: MultipartFile): String {
        val fileName =
            (file.originalFilename) ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid file name"
            )
        val targetLocation =
            Paths.get(uploadDir).resolve(SimpleDateFormat("yyyyMMdd").format(Date()) + '-' + fileName)

        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        return targetLocation.toString().replace("\\", "/")
    }
}