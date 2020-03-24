package org.covid19app.server.device

import org.assertj.core.api.Assertions
import org.covid19app.server.freshEventInfo
import org.covid19app.server.freshId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeviceControllerTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun getDevice() {
        val deviceId = freshId("device")

        val notRegisteredResponse = restTemplate.getForEntity<String>("/v1/device/$deviceId")
        Assertions.assertThat(notRegisteredResponse.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(notRegisteredResponse.body).isEqualTo("\"NOT_REGISTERED\"")

        val pushNotificationToken = freshId("notification")
        val deviceNotificationEvent = DeviceNotificationEvent(freshEventInfo(), deviceId, pushNotificationToken)
        restTemplate.put("/v1/device/$deviceId/notification", deviceNotificationEvent)

        val registeredResponse = restTemplate.getForEntity<String>("/v1/device/$deviceId")
        Assertions.assertThat(registeredResponse.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(registeredResponse.body).isEqualTo("\"REGISTERED\"")
    }

    @Test
    fun putDeviceNotification() {
        val deviceId = freshId("device")
        val pushNotificationToken = freshId("notification")
        val deviceNotificationEvent = DeviceNotificationEvent(freshEventInfo(), deviceId, pushNotificationToken)
        restTemplate.put("/v1/device/$deviceId/notification", deviceNotificationEvent)
    }
}
