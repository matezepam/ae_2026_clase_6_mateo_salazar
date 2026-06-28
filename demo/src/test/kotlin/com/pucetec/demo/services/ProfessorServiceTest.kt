package com.pucetec.demo.services

import com.pucetec.demo.dto.ProfessorRequest
import com.pucetec.demo.entities.Professor
import com.pucetec.demo.exceptions.ProfessorNotFoundException
import com.pucetec.demo.repositories.ProfessorRepository
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
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    @Test
    fun `createProfessor rechaza nombre en blanco`() {
        val request = ProfessorRequest(name = " ", email = "docente@puce.edu")

        assertThrows(IllegalArgumentException::class.java) {
            professorService.createProfessor(request)
        }
    }

    @Test
    fun `createProfessor devuelve respuesta del profesor guardado`() {
        val request = ProfessorRequest(name = "Martha Ruiz", email = "martha@puce.edu")
        val saved = Professor(id = 1L, name = "Martha Ruiz", email = "martha@puce.edu")
        `when`(professorRepository.save(any())).thenReturn(saved)

        val result = professorService.createProfessor(request)

        assertEquals(1L, result.id)
        assertEquals("Martha Ruiz", result.name)
        assertEquals("martha@puce.edu", result.email)
    }

    @Test
    fun `getAllProfessors transforma todos los profesores`() {
        val professors = listOf(
            Professor(id = 1L, name = "Martha Ruiz", email = "martha@puce.edu"),
            Professor(id = 2L, name = "Carlos Vega", email = "carlos@puce.edu")
        )
        `when`(professorRepository.findAll()).thenReturn(professors)

        val result = professorService.getAllProfessors()

        assertEquals(2, result.size)
        assertEquals("Martha Ruiz", result[0].name)
        assertEquals("Carlos Vega", result[1].name)
    }

    @Test
    fun `getProfessorById devuelve profesor encontrado`() {
        val professor = Professor(id = 3L, name = "Laura Paz", email = "laura@puce.edu")
        `when`(professorRepository.findById(3L)).thenReturn(Optional.of(professor))

        val result = professorService.getProfessorById(3L)

        assertEquals(3L, result.id)
        assertEquals("Laura Paz", result.name)
    }

    @Test
    fun `getProfessorById falla con id inexistente`() {
        `when`(professorRepository.findById(30L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFoundException::class.java) {
            professorService.getProfessorById(30L)
        }
    }

    @Test
    fun `updateProfessor rechaza nombre vacio`() {
        val request = ProfessorRequest(name = "", email = "x@puce.edu")

        assertThrows(IllegalArgumentException::class.java) {
            professorService.updateProfessor(1L, request)
        }
    }

    @Test
    fun `updateProfessor falla cuando no existe`() {
        val request = ProfessorRequest(name = "Nombre Nuevo", email = "nuevo@puce.edu")
        `when`(professorRepository.findById(60L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFoundException::class.java) {
            professorService.updateProfessor(60L, request)
        }
    }

    @Test
    fun `updateProfessor persiste valores actualizados`() {
        val request = ProfessorRequest(name = "Martha Actualizada", email = "martha.nueva@puce.edu")
        val existing = Professor(id = 1L, name = "Martha Ruiz", email = "martha@puce.edu")
        val updated = Professor(id = 1L, name = "Martha Actualizada", email = "martha.nueva@puce.edu")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(professorRepository.save(any())).thenReturn(updated)

        val result = professorService.updateProfessor(1L, request)

        assertEquals(1L, result.id)
        assertEquals("Martha Actualizada", result.name)
        assertEquals("martha.nueva@puce.edu", result.email)
    }

    @Test
    fun `deleteProfessor falla cuando falta el profesor`() {
        `when`(professorRepository.existsById(88L)).thenReturn(false)

        assertThrows(ProfessorNotFoundException::class.java) {
            professorService.deleteProfessor(88L)
        }
    }

    @Test
    fun `deleteProfessor borra el registro existente`() {
        `when`(professorRepository.existsById(1L)).thenReturn(true)

        professorService.deleteProfessor(1L)

        verify(professorRepository).deleteById(1L)
    }
}
