package com.pucetec.demo.entities

import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
@Table(name = "students")
class Student(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String = "",

    val email: String? = null,
)