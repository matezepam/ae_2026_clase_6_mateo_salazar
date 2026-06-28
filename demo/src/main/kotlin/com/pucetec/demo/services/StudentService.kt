package com.pucetec.demo.services

import com.pucetec.demo.dto.StudentRequest
import com.pucetec.demo.dto.StudentResponse
import com.pucetec.demo.exceptions.EmailAlreadyExistsException
import com.pucetec.demo.exceptions.StudentNotFoundException
import com.pucetec.demo.entities.Student
import com.pucetec.demo.mappers.toEntity
import com.pucetec.demo.mappers.toResponse
import com.pucetec.demo.repositories.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository
) {

    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    fun createStudent(request: StudentRequest): StudentResponse {
        logger.info("Creating Student ${request.name}")

        if (request.name.isBlank()) {
            throw IllegalArgumentException("Student name cannot be blank")
        }

        if (studentRepository.existsByEmail(request.email)) {
            logger.warn("Attempted to register duplicate email: ${request.email}")
            throw EmailAlreadyExistsException("El correo ${request.email} ya está registrado.")
        }

        val studentToSave = request.toEntity()

        val savedStudent = studentRepository.save(studentToSave)

        logger.info("Saved student with id ${savedStudent.id}")

        return savedStudent.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")

        val savedStudents = studentRepository.findAll()

        return savedStudents.map { it.toResponse() }
    }

    fun getStudentById(id: Long): StudentResponse {
        val student = studentRepository.findById(id).orElseThrow {
            StudentNotFoundException("Student with id $id was not found")
        }

        return student.toResponse()
    }

    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        if (request.name.isBlank()) {
            throw IllegalArgumentException("Student name cannot be blank")
        }

        studentRepository.findById(id).orElseThrow {
            StudentNotFoundException("Student with id $id was not found")
        }

        val studentToSave = Student(
            id = id,
            name = request.name,
            email = request.email
        )

        return studentRepository.save(studentToSave).toResponse()
    }

    fun deleteStudent(id: Long) {
        if (!studentRepository.existsById(id)) {
            throw StudentNotFoundException("Student with id $id was not found")
        }

        studentRepository.deleteById(id)
    }
}
