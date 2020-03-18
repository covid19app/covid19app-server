package org.covid19app.server.person

import org.covid19app.server.common.EventInfo
import java.time.LocalDate

data class PersonProfileEvent(
        val eventInfo: EventInfo,
        val personId: String,
        val name: String,
        val age: Int,
        val sex: Sex,
        val deleted: Boolean
)

data class PersonTravelHistoryEvent(
        val eventInfo: EventInfo,
        val personId: String,
        val country: String,
        val places: List<String>,
        val startDate: LocalDate,
        val endDate: LocalDate
)

// Symptoms taken from https://en.wikipedia.org/wiki/2019%E2%80%9320_coronavirus_pandemic
// All symptoms are floats with expected values between 0.0 (nothing) and 1.0 (big time). Whether it is a checkbox
// or 1 to 5 rating or whatever is frontend decision. The only difference is temperature. That is in Celsius. The app
// has to respect local system and show fahrenheit where appropriate. Just convert it to celsius before sending over.
data class PersonSymptomsEvent(
        val eventInfo: EventInfo,
        val personId: String,
        val feverInCelsius: Float,
        val dryCough: Float,
        val fatigue: Float,
        val sputumProduction: Float,
        val shortnessOfBreath: Float,
        val musclePainOrJointPain: Float,
        val soreThroat: Float,
        val headache: Float,
        val chills: Float,
        val nauseaOrVomiting: Float,
        val nasalCongestion: Float,
        val diarrhoea: Float,
        val haemoptysis: Float,
        val conjunctivalCongestion: Float,
        val other: Map<String, Float> = emptyMap()
)
