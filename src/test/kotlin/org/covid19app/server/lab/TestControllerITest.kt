package org.covid19app.server.lab

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerITest(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun `lab testing life cycle - pairing and result submission`() {
        val personId = freshId("patient")
        val testId = freshId("test")

        val notPairedResponse = restTemplate.getForEntity<LabResult>("/v1/test/$testId")
        assertThat(notPairedResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(notPairedResponse.body).isEqualTo(LabResult.UNKNOWN)

        val testPairEvent = TestPairEvent(freshEventInfo(), testId, personId)
        val testPairEventResponse = restTemplate.postForEntity<String>("/v1/test/$testId/pair", testPairEvent)
        assertThat(testPairEventResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(testPairEventResponse.body).isEqualTo("\"OK\"")

        val testResultEvent = TestResultEvent(freshEventInfo(), testId, LabResult.INFECTED)
        val testResultEventResponse = restTemplate.postForEntity<String>("/v1/test/$testId/result", testResultEvent)
        assertThat(testResultEventResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(testResultEventResponse.body).isEqualTo("\"OK\"")

        val pairedResponse = restTemplate.getForEntity<LabResult>("/v1/test/$testId")
        assertThat(pairedResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(pairedResponse.body).isEqualTo(LabResult.INFECTED)
    }

    @Test
    fun postTestResult() {
        val testId = freshId("test")
        val testResultEvent = TestResultEvent(freshEventInfo(), testId, LabResult.INFECTED)
        val response = restTemplate.postForEntity<String>("/v1/test/$testId/result", testResultEvent)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).containsIgnoringCase("ERROR")
    }
}
