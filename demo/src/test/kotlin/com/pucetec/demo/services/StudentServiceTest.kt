package com.pucetec.demo.services

import com.pucetec.demo.dto.StudentRequest
import com.pucetec.demo.entities.Student
import com.pucetec.demo.exceptions.EmailAlreadyExistsException
import com.pucetec.demo.exceptions.StudentNotFoundException
import com.pucetec.demo.repositories.StudentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    @Test
    fun `createStudent rechaza nombres vacios`() {
        val request = StudentRequest(name = "   ", email = "ana@puce.edu")

        assertThrows(IllegalArgumentException::class.java) {
            studentService.createStudent(request)
        }
    }

    @Test
    fun `createStudent rechaza correos ya registrados`() {
        val request = StudentRequest(name = "Ana Mora", email = "ana@puce.edu")
        `when`(studentRepository.existsByEmail("ana@puce.edu")).thenReturn(true)

        assertThrows(EmailAlreadyExistsException::class.java) {
            studentService.createStudent(request)
        }
    }

    @Test
    fun `createStudent guarda estudiante cuando el request es valido`() {
        val request = StudentRequest(name = "Ana Mora", email = "ana@puce.edu")
        val saved = Student(id = 1L, name = "Ana Mora", email = "ana@puce.edu")
        `when`(studentRepository.existsByEmail("ana@puce.edu")).thenReturn(false)
        `when`(studentRepository.save(any())).thenReturn(saved)

        val result = studentService.createStudent(request)

        assertEquals(1L, result.id)
        assertEquals("Ana Mora", result.name)
        assertEquals("ana@puce.edu", result.email)
    }

    @Test
    fun `getAllStudents devuelve respuestas mapeadas`() {
        val students = listOf(
            Student(id = 1L, name = "Ana", email = "ana@puce.edu"),
            Student(id = 2L, name = "Luis", email = "luis@puce.edu")
        )
        `when`(studentRepository.findAll()).thenReturn(students)

        val result = studentService.getAllStudents()

        assertEquals(2, result.size)
        assertEquals("Ana", result[0].name)
        assertEquals("Luis", result[1].name)
    }

    @Test
    fun `getAllStudents devuelve lista vacia sin registros`() {
        `when`(studentRepository.findAll()).thenReturn(emptyList())

        val result = studentService.getAllStudents()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getStudentById devuelve estudiante existente`() {
        val student = Student(id = 7L, name = "Marco", email = "marco@puce.edu")
        `when`(studentRepository.findById(7L)).thenReturn(Optional.of(student))

        val result = studentService.getStudentById(7L)

        assertEquals(7L, result.id)
        assertEquals("Marco", result.name)
    }

    @Test
    fun `getStudentById falla cuando no encuentra registro`() {
        `when`(studentRepository.findById(80L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            studentService.getStudentById(80L)
        }
    }

    @Test
    fun `updateStudent rechaza nombres vacios`() {
        val request = StudentRequest(name = "", email = "nuevo@puce.edu")

        assertThrows(IllegalArgumentException::class.java) {
            studentService.updateStudent(1L, request)
        }
    }

    @Test
    fun `updateStudent falla si el estudiante no existe`() {
        val request = StudentRequest(name = "Nuevo Nombre", email = "nuevo@puce.edu")
        `when`(studentRepository.findById(45L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            studentService.updateStudent(45L, request)
        }
    }

    @Test
    fun `updateStudent guarda los datos nuevos`() {
        val request = StudentRequest(name = "Ana Actualizada", email = "ana.nueva@puce.edu")
        val existing = Student(id = 1L, name = "Ana", email = "ana@puce.edu")
        val updated = Student(id = 1L, name = "Ana Actualizada", email = "ana.nueva@puce.edu")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(studentRepository.save(any())).thenReturn(updated)

        val result = studentService.updateStudent(1L, request)

        assertEquals(1L, result.id)
        assertEquals("Ana Actualizada", result.name)
        assertEquals("ana.nueva@puce.edu", result.email)
    }

    @Test
    fun `deleteStudent falla si no existe`() {
        `when`(studentRepository.existsById(90L)).thenReturn(false)

        assertThrows(StudentNotFoundException::class.java) {
            studentService.deleteStudent(90L)
        }
    }

    @Test
    fun `deleteStudent elimina por id cuando existe`() {
        `when`(studentRepository.existsById(1L)).thenReturn(true)

        studentService.deleteStudent(1L)

        verify(studentRepository).deleteById(1L)
    }
}
