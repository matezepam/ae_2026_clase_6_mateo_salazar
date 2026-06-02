package com.pucetec.demo.mappers

import com.pucetec.demo.dto.StudentRequest
import com.pucetec.demo.dto.StudentResponse
import com.pucetec.demo.entities.Student

fun StudentRequest.toEntity(): Student {
    return Student(
        name = name,
        email = email
    )
}

fun Student.toResponse(): StudentResponse {
    return StudentResponse(
        id = id,
        name = name,
        email = email ?: ""
    )
}