package com.polidea.salove.domain.reservation

import org.springframework.stereotype.Service

@Service
class Reservation {
    
    fun create(id: Integer, start: Long, end: Long) {
        println("hello")
    }

}
