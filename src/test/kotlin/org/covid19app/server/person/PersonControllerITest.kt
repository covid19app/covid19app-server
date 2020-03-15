package org.covid19app.server.person

import org.assertj.core.api.Assertions.assertThat
import org.covid19app.server.common.EventInfo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import java.time.Instant
import java.time.LocalDate

//@org.springframework.test.context.ActiveProfiles("test_mysql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerITest(@Autowired val restTemplate: TestRestTemplate) {
    val eventInfo = EventInfo("event1", "person1", "device1", Instant.now())

    @Test
    fun getPerson() {
        val notRegisteredResponse = restTemplate.getForEntity<String>("/v1/person/personForGetPerson")
        assertThat(notRegisteredResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(notRegisteredResponse.body).isEqualTo("\"NOT REGISTERED\"")

        val profileEvent = PersonProfileEvent(eventInfo, 42, Sex.NON_BINARY, "John Doe", false)
        restTemplate.put("/v1/person/personForGetPerson/profile", profileEvent)

        val registeredResponse = restTemplate.getForEntity<String>("/v1/person/personForGetPerson")
        assertThat(registeredResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(registeredResponse.body).isEqualTo("\"REGISTERED\"")
    }

    @Test
    fun putProfile() {
        val profileEvent = PersonProfileEvent(eventInfo, 42, Sex.NON_BINARY, "John Doe", false)
        restTemplate.put("/v1/person/person1/profile", profileEvent)
    }

    @Test
    fun putTravelHistory() {
        val travelHistoryEvent = PersonTravelHistoryEvent(eventInfo,
                "China", listOf("Wuhan", "Peking"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-31"))
        restTemplate.put("/v1/person/person1/travelHistory", travelHistoryEvent)
    }

    @Test
    fun postSymptoms() {
        val emptySymptomsEvent = PersonSymptomsEvent(eventInfo, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                emptyMap())

        val goodSymptomsEvent = emptySymptomsEvent.copy(feverInCelsius = 37.0f)
        val goodResponse = restTemplate.postForEntity<NextSteps>("/v1/person/person1/symptoms", goodSymptomsEvent)
        assertThat(goodResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(goodResponse.body?.action).isEqualTo(Action.STAY_HEALTHY)

        val badSymptomsEvent = emptySymptomsEvent.copy(feverInCelsius = 38.0f)
        val badResponse = restTemplate.postForEntity<NextSteps>("/v1/person/person1/symptoms", badSymptomsEvent)
        assertThat(badResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(badResponse.body?.action).isEqualTo(Action.GET_TESTED)
    }
}
