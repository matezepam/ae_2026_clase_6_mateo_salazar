package com.pucetec.demo.mappers

import com.pucetec.demo.dto.StudentRequest
import com.pucetec.demo.dto.StudentResponse
import com.pucetec.demo.dto.ProfessorRequest
import com.pucetec.demo.dto.ProfessorResponse
import com.pucetec.demo.dto.SubjectResponse
import com.pucetec.demo.dto.EnrollmentResponse
import com.pucetec.demo.entities.Professor
import com.pucetec.demo.entities.Student
import com.pucetec.demo.entities.Subject
import com.pucetec.demo.entities.Enrollment

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

fun ProfessorRequest.toEntity(): Professor {
    return Professor(
        name = name,
        email = email
    )
}

fun Professor.toResponse(): ProfessorResponse {
    return ProfessorResponse(
        id = id,
        name = name,
        email = email ?: ""
    )
}

fun Subject.toResponse(): SubjectResponse {
    return SubjectResponse(
        id = id,
        name = name,
        code = code,
        professor = professor.toResponse()
    )
}

fun Enrollment.toResponse(): EnrollmentResponse {
    return EnrollmentResponse(
        id = id,
        student = student.toResponse(),
        subject = subject.toResponse(),
        status = status
    )
}
