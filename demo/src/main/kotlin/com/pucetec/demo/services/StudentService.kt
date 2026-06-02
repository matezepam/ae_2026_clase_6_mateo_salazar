package com.pucetec.demo.services

import com.pucetec.demo.dto.StudentRequest
import com.pucetec.demo.dto.StudentResponse
import com.pucetec.demo.exceptions.EmailAlreadyExistsException
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
}