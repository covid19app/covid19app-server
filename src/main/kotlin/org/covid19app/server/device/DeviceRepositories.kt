package org.covid19app.server.device

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class DeviceEntity(
        @Id
        val deviceId: String,
        var pushNotificationToken: String?,
        var defaultPersonId: String?
)

interface DeviceRepository : JpaRepository<DeviceEntity, String>
