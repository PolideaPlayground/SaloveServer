package com.polidea.salove.web.rest

import com.polidea.salove.domain.reservation.Reservation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


data class ReservationRequest(var id: Int, var start: Long, var end: Long)

@RestController
@RequestMapping("rooms")
class ReservationsController(val reservation: Reservation) {

    data class ReservationRequest(var id: Int, var start: Long, var end: Long)
    
    @PostMapping("/{reservation}")
    fun findByLastName(@RequestBody body: ReservationRequest)
            = reservation.create(body.id, body.start, body.end)
}



