package com.pucetec.demo.services

import com.pucetec.demo.dto.SubjectRequest
import com.pucetec.demo.entities.Professor
import com.pucetec.demo.entities.Subject
import com.pucetec.demo.exceptions.ProfessorNotFoundException
import com.pucetec.demo.exceptions.SubjectNotFoundException
import com.pucetec.demo.repositories.ProfessorRepository
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
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    private val professor = Professor(id = 1L, name = "Martha Ruiz", email = "martha@puce.edu")

    @Test
    fun `createSubject rechaza nombre vacio`() {
        val request = SubjectRequest(name = " ", code = "MAT100", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject rechaza codigo vacio`() {
        val request = SubjectRequest(name = "Matematica", code = "", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject falla si el profesor no existe`() {
        val request = SubjectRequest(name = "Matematica", code = "MAT100", professorId = 70L)
        `when`(professorRepository.findById(70L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFoundException::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject guarda la materia valida`() {
        val request = SubjectRequest(name = "Matematica", code = "MAT100", professorId = 1L)
        val saved = Subject(id = 5L, name = "Matematica", code = "MAT100", professor = professor)
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any())).thenReturn(saved)

        val result = subjectService.createSubject(request)

        assertEquals(5L, result.id)
        assertEquals("Matematica", result.name)
        assertEquals("MAT100", result.code)
        assertEquals(1L, result.professor.id)
    }

    @Test
    fun `getAllSubjects devuelve materias mapeadas`() {
        val subjects = listOf(
            Subject(id = 1L, name = "Fisica", code = "FIS100", professor = professor),
            Subject(id = 2L, name = "Quimica", code = "QUI100", professor = professor)
        )
        `when`(subjectRepository.findAll()).thenReturn(subjects)

        val result = subjectService.getAllSubjects()

        assertEquals(2, result.size)
        assertEquals("Fisica", result[0].name)
        assertEquals("Quimica", result[1].name)
    }

    @Test
    fun `getSubjectById devuelve materia encontrada`() {
        val subject = Subject(id = 8L, name = "Historia", code = "HIS100", professor = professor)
        `when`(subjectRepository.findById(8L)).thenReturn(Optional.of(subject))

        val result = subjectService.getSubjectById(8L)

        assertEquals(8L, result.id)
        assertEquals("Historia", result.name)
    }

    @Test
    fun `getSubjectById falla si no existe`() {
        `when`(subjectRepository.findById(44L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFoundException::class.java) {
            subjectService.getSubjectById(44L)
        }
    }

    @Test
    fun `updateSubject rechaza nombre vacio`() {
        val request = SubjectRequest(name = "", code = "MAT200", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject rechaza codigo vacio`() {
        val request = SubjectRequest(name = "Matematica", code = " ", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject falla si la materia no existe`() {
        val request = SubjectRequest(name = "Matematica", code = "MAT200", professorId = 1L)
        `when`(subjectRepository.findById(91L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFoundException::class.java) {
            subjectService.updateSubject(91L, request)
        }
    }

    @Test
    fun `updateSubject falla si el profesor asignado no existe`() {
        val request = SubjectRequest(name = "Matematica", code = "MAT200", professorId = 77L)
        val existing = Subject(id = 1L, name = "Matematica", code = "MAT100", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(professorRepository.findById(77L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFoundException::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject guarda cambios validos`() {
        val request = SubjectRequest(name = "Matematica II", code = "MAT200", professorId = 1L)
        val existing = Subject(id = 1L, name = "Matematica", code = "MAT100", professor = professor)
        val updated = Subject(id = 1L, name = "Matematica II", code = "MAT200", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any())).thenReturn(updated)

        val result = subjectService.updateSubject(1L, request)

        assertEquals(1L, result.id)
        assertEquals("Matematica II", result.name)
        assertEquals("MAT200", result.code)
    }

    @Test
    fun `deleteSubject falla cuando no existe`() {
        `when`(subjectRepository.existsById(63L)).thenReturn(false)

        assertThrows(SubjectNotFoundException::class.java) {
            subjectService.deleteSubject(63L)
        }
    }

    @Test
    fun `deleteSubject elimina materia existente`() {
        `when`(subjectRepository.existsById(1L)).thenReturn(true)

        subjectService.deleteSubject(1L)

        verify(subjectRepository).deleteById(1L)
    }
}
