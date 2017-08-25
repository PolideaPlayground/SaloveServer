package com.polidea.salove.domain.reservation

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.google.api.services.calendar.model.Events
import org.springframework.stereotype.Service
import java.util.*


@Service
class Reservation(val calendar: Calendar) {

    val CALENDAR_ID: String = "primary"

    fun create(roomId: Int) {
        val event = Event()
        event.summary = "Room $roomId ad-hoc reservation!"
        event.description = roomId.toString()

        val now: Date = Date()
        setStartDate(event, now)
        setEndTime(event, now)
        calendar.events().insert(CALENDAR_ID, event).execute()
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

    private fun getNextFullHour(now: Date): Date {
        val cal = java.util.Calendar.getInstance()
        cal.time = now
        cal.add(java.util.Calendar.HOUR_OF_DAY, 1)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        return cal.time
    }


    data class EventResponse(val start: Long, val end: Long, val summary: String, val roomId: Long?)

    fun getTodaysEvents(): List<EventResponse> {
        val now = Date()

        val events: Events = calendar.events().list(CALENDAR_ID).setTimeMin(DateTime(now)).setTimeMax(DateTime(getTodaysMaxHour(now)))
                .setOrderBy("startTime").setSingleEvents(true).execute()

        return events.items.map {
            EventResponse(it.start.dateTime.value, it.end.dateTime.value, it.summary, it.description.toLongOrNull())
        }
    }

    private fun getTodaysMaxHour(now: Date): Date {
        val cal = java.util.Calendar.getInstance()
        cal.time = now
        cal.add(java.util.Calendar.HOUR_OF_DAY, 23)
        cal.set(java.util.Calendar.MINUTE, 59)
        cal.set(java.util.Calendar.SECOND, 59)
        return cal.time
    }
}
