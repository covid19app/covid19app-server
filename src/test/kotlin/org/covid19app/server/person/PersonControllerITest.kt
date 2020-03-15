package org.covid19app.server.person

import org.assertj.core.api.Assertions.assertThat
import org.covid19app.server.freshEventInfo
import org.covid19app.server.freshId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import java.time.LocalDate

//@org.springframework.test.context.ActiveProfiles("test_mysql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerITest(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun getPerson() {
        val personId = freshId("person")
        val deviceId = freshId("device")

        val notRegisteredResponse = restTemplate.getForEntity<String>("/v1/person/$personId")
        assertThat(notRegisteredResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(notRegisteredResponse.body).isEqualTo("\"NOT REGISTERED\"")

        val eventInfo = freshEventInfo(personId, deviceId)
        val profileEvent = PersonProfileEvent(eventInfo, 42, Sex.NON_BINARY, "John Doe", false)
        restTemplate.put("/v1/person/$personId/profile", profileEvent)

        val registeredResponse = restTemplate.getForEntity<String>("/v1/person/$personId")
        assertThat(registeredResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(registeredResponse.body).isEqualTo("\"REGISTERED\"")
    }

    @Test
    fun putProfile() {
        val eventInfo = freshEventInfo()
        val profileEvent = PersonProfileEvent(eventInfo, 42, Sex.NON_BINARY, "John Doe", false)
        restTemplate.put("/v1/person/${eventInfo.personId}/profile", profileEvent)
    }

    @Test
    fun putTravelHistory() {
        val eventInfo = freshEventInfo()
        val travelHistoryEvent = PersonTravelHistoryEvent(eventInfo,
                "China", listOf("Wuhan", "Peking"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-31"))
        restTemplate.put("/v1/person/${eventInfo.personId}/travelHistory", travelHistoryEvent)
    }

    @Test
    fun postSymptoms() {
        val eventInfo = freshEventInfo()
        val personId = eventInfo.personId
        val emptySymptomsEvent = PersonSymptomsEvent(eventInfo, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                emptyMap())

        val goodSymptomsEvent = emptySymptomsEvent.copy(feverInCelsius = 37.0f)
        val goodResponse = restTemplate.postForEntity<NextSteps>("/v1/person/$personId/symptoms", goodSymptomsEvent)
        assertThat(goodResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(goodResponse.body?.action).isEqualTo(Action.STAY_HEALTHY)

        val badSymptomsEvent = emptySymptomsEvent.copy(feverInCelsius = 38.0f)
        val badResponse = restTemplate.postForEntity<NextSteps>("/v1/person/$personId/symptoms", badSymptomsEvent)
        assertThat(badResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(badResponse.body?.action).isEqualTo(Action.GET_TESTED)
    }
}
