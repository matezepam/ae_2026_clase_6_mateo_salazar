package com.pucetec.demo.services

import com.pucetec.demo.dto.SubjectRequest
import com.pucetec.demo.dto.SubjectResponse
import com.pucetec.demo.entities.Subject
import com.pucetec.demo.exceptions.ProfessorNotFoundException
import com.pucetec.demo.exceptions.SubjectNotFoundException
import com.pucetec.demo.mappers.toResponse
import com.pucetec.demo.repositories.ProfessorRepository
import com.pucetec.demo.repositories.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository
) {

    fun createSubject(request: SubjectRequest): SubjectResponse {
        validateSubject(request)

        val professor = professorRepository.findById(request.professorId).orElseThrow {
            ProfessorNotFoundException("Professor with id ${request.professorId} was not found")
        }

        val subjectToSave = Subject(
            name = request.name,
            code = request.code,
            professor = professor
        )

        return subjectRepository.save(subjectToSave).toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        return subjectRepository.findAll().map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        val subject = subjectRepository.findById(id).orElseThrow {
            SubjectNotFoundException("Subject with id $id was not found")
        }

        return subject.toResponse()
    }

    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        validateSubject(request)

        subjectRepository.findById(id).orElseThrow {
            SubjectNotFoundException("Subject with id $id was not found")
        }

        val professor = professorRepository.findById(request.professorId).orElseThrow {
            ProfessorNotFoundException("Professor with id ${request.professorId} was not found")
        }

        val subjectToSave = Subject(
            id = id,
            name = request.name,
            code = request.code,
            professor = professor
        )

        return subjectRepository.save(subjectToSave).toResponse()
    }

    fun deleteSubject(id: Long) {
        if (!subjectRepository.existsById(id)) {
            throw SubjectNotFoundException("Subject with id $id was not found")
        }

        subjectRepository.deleteById(id)
    }

    private fun validateSubject(request: SubjectRequest) {
        if (request.name.isBlank()) {
            throw IllegalArgumentException("Subject name cannot be blank")
        }

        if (request.code.isBlank()) {
            throw IllegalArgumentException("Subject code cannot be blank")
        }
    }
}
