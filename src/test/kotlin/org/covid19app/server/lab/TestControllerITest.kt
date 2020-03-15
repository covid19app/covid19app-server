package org.covid19app.server.lab

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerITest(@Autowired val restTemplate: TestRestTemplate) {
    val eventInfo = EventInfo("event1", "person1", "device1", Instant.now())

    @Test
    fun `lab testing life cycle - pairing and result submission`() {
        val testId = "test1"

        val notPairedResponse = restTemplate.getForEntity<LabResult>("/v1/test/$testId/result")
        assertThat(notPairedResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(notPairedResponse.body).isNull()

        val pairEvent = TestPairEvent(eventInfo, testId, "person1")
        val pairEventResponse = restTemplate.postForEntity<String>("/v1/test/$testId/pair", pairEvent)
        assertThat(pairEventResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(pairEventResponse.body).isEqualTo("OK")

        val resultEvent = TestResultEvent(eventInfo, testId, LabResult.INFECTED)
        val resultEventResponse = restTemplate.postForEntity<String>("/v1/test/$testId/result", resultEvent)
        assertThat(resultEventResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resultEventResponse.body).isEqualTo("OK")

        val pairedResponse = restTemplate.getForEntity<LabResult>("/v1/test/$testId/result")
        assertThat(pairedResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(pairedResponse.body).isEqualTo(LabResult.INFECTED)
    }

    @Test
    fun postResult() {
        val resultEvent = TestResultEvent(eventInfo, "test11", LabResult.INFECTED)
        val response = restTemplate.postForEntity<String>("/v1/test/test11/result", resultEvent)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).containsIgnoringCase("ERROR")
    }
}
