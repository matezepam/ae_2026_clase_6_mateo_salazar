package com.pucetec.demo.services

import com.pucetec.demo.dto.EnrollmentRequest
import com.pucetec.demo.dto.EnrollmentResponse
import com.pucetec.demo.dto.EnrollmentUpdateRequest
import com.pucetec.demo.entities.Enrollment
import com.pucetec.demo.exceptions.EnrollmentNotFoundException
import com.pucetec.demo.exceptions.StudentNotFoundException
import com.pucetec.demo.exceptions.SubjectNotFoundException
import com.pucetec.demo.mappers.toResponse
import com.pucetec.demo.repositories.EnrollmentRepository
import com.pucetec.demo.repositories.StudentRepository
import com.pucetec.demo.repositories.SubjectRepository
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) {

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        val student = studentRepository.findById(request.studentId).orElseThrow {
            StudentNotFoundException("Student with id ${request.studentId} was not found")
        }

        val subject = subjectRepository.findById(request.subjectId).orElseThrow {
            SubjectNotFoundException("Subject with id ${request.subjectId} was not found")
        }

        val enrollmentToSave = Enrollment(
            student = student,
            subject = subject,
            status = "INSCRITO"
        )

        return enrollmentRepository.save(enrollmentToSave).toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        val enrollment = enrollmentRepository.findById(id).orElseThrow {
            EnrollmentNotFoundException("Enrollment with id $id was not found")
        }

        return enrollment.toResponse()
    }

    fun updateEnrollment(id: Long, request: EnrollmentUpdateRequest): EnrollmentResponse {
        if (request.status.isBlank()) {
            throw IllegalArgumentException("Enrollment status cannot be blank")
        }

        val enrollment = enrollmentRepository.findById(id).orElseThrow {
            EnrollmentNotFoundException("Enrollment with id $id was not found")
        }

        val enrollmentToSave = Enrollment(
            id = enrollment.id,
            student = enrollment.student,
            subject = enrollment.subject,
            status = request.status,
            createdAt = enrollment.createdAt
        )

        return enrollmentRepository.save(enrollmentToSave).toResponse()
    }

    fun deleteEnrollment(id: Long) {
        if (!enrollmentRepository.existsById(id)) {
            throw EnrollmentNotFoundException("Enrollment with id $id was not found")
        }

        enrollmentRepository.deleteById(id)
    }
}
