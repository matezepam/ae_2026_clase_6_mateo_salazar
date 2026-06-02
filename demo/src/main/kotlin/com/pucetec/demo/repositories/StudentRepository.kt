package com.pucetec.demo.repositories

import com.pucetec.demo.entities.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, Long> {

    fun existsByEmail(email: String): Boolean
}