package com.pucetec.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StudentsApplication

fun main(args: Array<String>) {
    runApplication<StudentsApplication>(*args)
}