package com.polidea.salove.domain.reservation

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import org.springframework.stereotype.Service

@Service
class Reservation(val calendar: Calendar) {

    fun create(id: Int, eventStart: Long, eventEnd: Long) {

        val now = DateTime(System.currentTimeMillis())
        val events = calendar.events().list("primary").setMaxResults(10).setTimeMin(now)
                .setOrderBy("startTime").setSingleEvents(true).execute()
        val items = events.getItems()
        if (items.size == 0) {
            println("No upcoming events found.")
        } else {
            println("Upcoming events")
            for (event in items) {
                var start: DateTime? = event.getStart().getDateTime()
                if (start == null) {
                    start = event.getStart().getDate()
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start)
            }
        }
    }
}
