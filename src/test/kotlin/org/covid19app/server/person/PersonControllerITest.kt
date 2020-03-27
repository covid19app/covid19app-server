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

        val notRegisteredResponse = restTemplate.getForEntity<String>("/v1/person/$personId")
        assertThat(notRegisteredResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(notRegisteredResponse.body).isEqualTo("\"NOT_REGISTERED\"")

        val personProfileEvent = PersonProfileEvent(freshEventInfo(),
                personId, "John Doe", 42, Sex.NON_BINARY, "en-US", null)
        restTemplate.postForEntity<String>("/v1/person/$personId/profile", personProfileEvent)

        val registeredResponse = restTemplate.getForEntity<String>("/v1/person/$personId")
        assertThat(registeredResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(registeredResponse.body).isEqualTo("\"REGISTERED\"")
    }

    @Test
    fun postPersonProfile() {
        val personId = freshId("person")
        val personProfileEvent = PersonProfileEvent(freshEventInfo(),
                personId, "John Doe", 42, Sex.NON_BINARY, "en-US", null)
        restTemplate.postForEntity<String>("/v1/person/$personId/profile", personProfileEvent)
    }

    @Test
    fun postPersonTravelHistory() {
        val personId = freshId("person")
        val personTravelHistoryEvent = PersonTravelHistoryEvent(freshEventInfo(), personId, "China",
                listOf("Wuhan"), LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-31"))
        restTemplate.postForEntity<String>("/v1/person/$personId/travelHistory", personTravelHistoryEvent)
    }

    @Test
    fun postPersonSymptoms() {
        val personId = freshId("person")
        val emptyPersonSymptomsEvent = PersonSymptomsEvent(freshEventInfo(), personId, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, emptyMap())

        val goodSymptomsEvent = emptyPersonSymptomsEvent.copy(feverInCelsius = 37.0f)
        val goodResponse = restTemplate.postForEntity<NextSteps>("/v1/person/$personId/symptoms", goodSymptomsEvent)
        assertThat(goodResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(goodResponse.body?.text).containsIgnoringCase("go on")

        val badSymptomsEvent = emptyPersonSymptomsEvent.copy(feverInCelsius = 38.0f)
        val badResponse = restTemplate.postForEntity<NextSteps>("/v1/person/$personId/symptoms", badSymptomsEvent)
        assertThat(badResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(badResponse.body?.externalLinkTitle).isEqualTo("Go to Lab!")
    }
}
