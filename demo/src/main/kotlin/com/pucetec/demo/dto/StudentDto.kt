package com.pucetec.demo.dto

data class StudentRequest(
    val name: String,
    val email: String
)

data class StudentResponse(
    val id: Long,
    val name: String,
    val email: String
)

data class ProfessorRequest(
    val name: String,
    val email: String
)

data class ProfessorResponse(
    val id: Long,
    val name: String,
    val email: String
)

data class SubjectRequest(
    val name: String,
    val code: String,
    val professorId: Long
)

data class SubjectResponse(
    val id: Long,
    val name: String,
    val code: String,
    val professor: ProfessorResponse
)

data class EnrollmentRequest(
    val studentId: Long,
    val subjectId: Long
)

data class EnrollmentUpdateRequest(
    val status: String
)

data class EnrollmentResponse(
    val id: Long,
    val student: StudentResponse,
    val subject: SubjectResponse,
    val status: String
)
