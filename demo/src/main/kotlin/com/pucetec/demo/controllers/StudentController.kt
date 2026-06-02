package com.pucetec.demo.controllers

import com.pucetec.demo.dto.StudentRequest
import com.pucetec.demo.dto.StudentResponse
import com.pucetec.demo.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StudentController(
    private val studentService: StudentService
) {

    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping("/api/students")
    fun createStudent(
        @RequestBody request: StudentRequest
    ): StudentResponse {
        logger.info("Creating student ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping("/api/students")
    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        return studentService.getAllStudents()
    }
}