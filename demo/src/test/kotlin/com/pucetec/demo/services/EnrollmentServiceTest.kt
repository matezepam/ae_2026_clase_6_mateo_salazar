package com.pucetec.demo.services

import com.pucetec.demo.dto.EnrollmentRequest
import com.pucetec.demo.dto.EnrollmentUpdateRequest
import com.pucetec.demo.entities.Enrollment
import com.pucetec.demo.entities.Professor
import com.pucetec.demo.entities.Student
import com.pucetec.demo.entities.Subject
import com.pucetec.demo.exceptions.EnrollmentNotFoundException
import com.pucetec.demo.exceptions.StudentNotFoundException
import com.pucetec.demo.exceptions.SubjectNotFoundException
import com.pucetec.demo.repositories.EnrollmentRepository
import com.pucetec.demo.repositories.StudentRepository
import com.pucetec.demo.repositories.SubjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    private val professor = Professor(id = 1L, name = "Martha Ruiz", email = "martha@puce.edu")
    private val student = Student(id = 1L, name = "Ana Mora", email = "ana@puce.edu")
    private val subject = Subject(id = 1L, name = "Matematica", code = "MAT100", professor = professor)

    @Test
    fun `createEnrollment falla si el estudiante no existe`() {
        val request = EnrollmentRequest(studentId = 40L, subjectId = 1L)
        `when`(studentRepository.findById(40L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment falla si la materia no existe`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 41L)
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(41L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFoundException::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment crea inscripcion con estado inicial`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        val saved = Enrollment(id = 10L, student = student, subject = subject, status = "INSCRITO")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any())).thenReturn(saved)

        val result = enrollmentService.createEnrollment(request)

        assertEquals(10L, result.id)
        assertEquals("INSCRITO", result.status)
        assertEquals(1L, result.student.id)
        assertEquals(1L, result.subject.id)
    }

    @Test
    fun `getAllEnrollments devuelve inscripciones mapeadas`() {
        val now = LocalDateTime.now()
        val enrollments = listOf(
            Enrollment(id = 1L, student = student, subject = subject, status = "INSCRITO", createdAt = now),
            Enrollment(id = 2L, student = student, subject = subject, status = "APROBADO", createdAt = now)
        )
        `when`(enrollmentRepository.findAll()).thenReturn(enrollments)

        val result = enrollmentService.getAllEnrollments()

        assertEquals(2, result.size)
        assertEquals("INSCRITO", result[0].status)
        assertEquals("APROBADO", result[1].status)
    }

    @Test
    fun `getEnrollmentById devuelve inscripcion encontrada`() {
        val enrollment = Enrollment(id = 4L, student = student, subject = subject, status = "INSCRITO")
        `when`(enrollmentRepository.findById(4L)).thenReturn(Optional.of(enrollment))

        val result = enrollmentService.getEnrollmentById(4L)

        assertEquals(4L, result.id)
        assertEquals("INSCRITO", result.status)
    }

    @Test
    fun `getEnrollmentById falla con id inexistente`() {
        `when`(enrollmentRepository.findById(55L)).thenReturn(Optional.empty())

        assertThrows(EnrollmentNotFoundException::class.java) {
            enrollmentService.getEnrollmentById(55L)
        }
    }

    @Test
    fun `updateEnrollment rechaza estado vacio`() {
        val request = EnrollmentUpdateRequest(status = " ")

        assertThrows(IllegalArgumentException::class.java) {
            enrollmentService.updateEnrollment(1L, request)
        }
    }

    @Test
    fun `updateEnrollment falla cuando no existe`() {
        val request = EnrollmentUpdateRequest(status = "RETIRADO")
        `when`(enrollmentRepository.findById(81L)).thenReturn(Optional.empty())

        assertThrows(EnrollmentNotFoundException::class.java) {
            enrollmentService.updateEnrollment(81L, request)
        }
    }

    @Test
    fun `updateEnrollment guarda nuevo estado`() {
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        val now = LocalDateTime.now()
        val current = Enrollment(id = 1L, student = student, subject = subject, status = "INSCRITO", createdAt = now)
        val updated = Enrollment(id = 1L, student = student, subject = subject, status = "APROBADO", createdAt = now)
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(current))
        `when`(enrollmentRepository.save(any())).thenReturn(updated)

        val result = enrollmentService.updateEnrollment(1L, request)

        assertEquals(1L, result.id)
        assertEquals("APROBADO", result.status)
    }

    @Test
    fun `deleteEnrollment falla si no existe`() {
        `when`(enrollmentRepository.existsById(77L)).thenReturn(false)

        assertThrows(EnrollmentNotFoundException::class.java) {
            enrollmentService.deleteEnrollment(77L)
        }
    }

    @Test
    fun `deleteEnrollment elimina la inscripcion existente`() {
        `when`(enrollmentRepository.existsById(1L)).thenReturn(true)

        enrollmentService.deleteEnrollment(1L)

        verify(enrollmentRepository).deleteById(1L)
    }
}
