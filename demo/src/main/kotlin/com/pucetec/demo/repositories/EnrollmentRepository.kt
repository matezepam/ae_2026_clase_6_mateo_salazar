package com.pucetec.demo.repositories

import com.pucetec.demo.entities.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository : JpaRepository<Enrollment, Long>
