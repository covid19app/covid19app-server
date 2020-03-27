package org.covid19app.server.device

import org.covid19app.server.common.RegistrationStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// TODO: Publish all events to kafka for later use!

@CrossOrigin
@RestController
@RequestMapping("/v1/device")
class DeviceController(@Autowired val deviceRepository: DeviceRepository) {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping("/{deviceId}")
    fun getDevice(@PathVariable deviceId: String): RegistrationStatus {
        return when (deviceRepository.existsById(deviceId)) {
            true -> RegistrationStatus.REGISTERED
            false -> RegistrationStatus.NOT_REGISTERED
        }
    }

    @PostMapping("/{deviceId}/notification")
    fun postDeviceNotification(
            @PathVariable deviceId: String, @RequestBody deviceNotificationEvent: DeviceNotificationEvent): String {
        log.info(">>>> postDeviceNotification($deviceNotificationEvent)")
//        assert(deviceId == deviceNotificationEvent.deviceId)
        val deviceEntity =
                DeviceEntity(deviceNotificationEvent.deviceId, deviceNotificationEvent.pushNotificationToken, null)
        deviceRepository.save(deviceEntity)
        return "\"OK\""
    }
}
