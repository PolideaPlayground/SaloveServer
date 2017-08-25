package com.polidea.salove.domain.reservation

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import org.springframework.stereotype.Service
import java.util.*


@Service
class Reservation(val calendar: Calendar) {

    val CALENDAR_ID: String = "primary"

    fun create(roomId: Int) {
        val event = Event()
        event.summary = "Room $roomId reservation!"

        val now: Date = Date()
        setStartDate(event, now)
        setEndTime(event, now)
        calendar.events().insert(CALENDAR_ID, event).execute()

        read()
    }

    private fun setEndTime(event: Event, now: Date) {
        event.end = EventDateTime()
        event.end.dateTime = DateTime(getNextFullHour(now))
        event.end.timeZone = "Europe/Warsaw"
    }

    private fun setStartDate(event: Event, now: Date) {
        event.start = EventDateTime()
        event.start.dateTime = DateTime(now)
        event.start.timeZone = "Europe/Warsaw"
    }

    private fun getNextFullHour(now: Date): Date? {
        val cal = java.util.Calendar.getInstance()
        cal.time = now
        cal.add(java.util.Calendar.HOUR_OF_DAY, 1) // adds one hour
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        val nextFullHour = cal.getTime()
        return nextFullHour
    }

    fun read() {
        val now = DateTime(System.currentTimeMillis())

        val events = calendar.events().list(CALENDAR_ID).setMaxResults(10).setTimeMin(now)
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
