package com.polidea.salove.web.rest

import com.polidea.salove.domain.reservation.Reservation
import org.springframework.web.bind.annotation.*


data class ReservationRequest(var id: Int)

@RestController
@RequestMapping("rooms")
class ReservationsController(val reservation: Reservation) {

    data class ReservationRequest(var roomId: Int)

    @PostMapping("/{reservation}")
    fun createEvent(@RequestBody body: ReservationRequest) = reservation.create(body.roomId)

    @GetMapping("/{reservation}")
    fun getTodaysEvents() = reservation.getTodaysEvents()
}



