package com.pucetec.demo.exceptions

class StudentNotFoundException(
    message: String
) : RuntimeException(message)

class ProfessorNotFoundException(
    message: String
) : RuntimeException(message)

class SubjectNotFoundException(
    message: String
) : RuntimeException(message)

class EnrollmentNotFoundException(
    message: String
) : RuntimeException(message)
