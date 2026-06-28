package com.pucetec.demo.services

import com.pucetec.demo.dto.ProfessorRequest
import com.pucetec.demo.dto.ProfessorResponse
import com.pucetec.demo.entities.Professor
import com.pucetec.demo.exceptions.ProfessorNotFoundException
import com.pucetec.demo.mappers.toEntity
import com.pucetec.demo.mappers.toResponse
import com.pucetec.demo.repositories.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository
) {

    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        if (request.name.isBlank()) {
            throw IllegalArgumentException("Professor name cannot be blank")
        }

        return professorRepository.save(request.toEntity()).toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        return professorRepository.findAll().map { it.toResponse() }
    }

    fun getProfessorById(id: Long): ProfessorResponse {
        val professor = professorRepository.findById(id).orElseThrow {
            ProfessorNotFoundException("Professor with id $id was not found")
        }

        return professor.toResponse()
    }

    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        if (request.name.isBlank()) {
            throw IllegalArgumentException("Professor name cannot be blank")
        }

        professorRepository.findById(id).orElseThrow {
            ProfessorNotFoundException("Professor with id $id was not found")
        }

        val professorToSave = Professor(
            id = id,
            name = request.name,
            email = request.email
        )

        return professorRepository.save(professorToSave).toResponse()
    }

    fun deleteProfessor(id: Long) {
        if (!professorRepository.existsById(id)) {
            throw ProfessorNotFoundException("Professor with id $id was not found")
        }

        professorRepository.deleteById(id)
    }
}
