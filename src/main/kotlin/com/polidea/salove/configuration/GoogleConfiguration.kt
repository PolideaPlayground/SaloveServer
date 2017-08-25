package com.polidea.salove.configuration

import com.polidea.salove.infrastructure.GoogleRegistration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CalendarConfiguration {

    @Bean
    fun calendar() : com.google.api.services.calendar.Calendar = GoogleRegistration.getCalendarService()
}
