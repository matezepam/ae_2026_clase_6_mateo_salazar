package com.pucetec.demo.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "enrollments")
class Enrollment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    val student: Student = Student(),
    @ManyToOne
    val subject: Subject = Subject(),
    val status: String = "INSCRITO",
    val createdAt: LocalDateTime = LocalDateTime.now()
)
