package com.pucetec.demo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailAlreadyExistsException(
        e: EmailAlreadyExistsException
    ): ResponseEntity<ErrorResponse> {

        val errorResponse = ErrorResponse(
            message = "Email already exists: ${e.message}"
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }
}

data class ErrorResponse(
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)