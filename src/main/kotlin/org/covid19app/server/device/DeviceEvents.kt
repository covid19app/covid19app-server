package org.covid19app.server.device

import org.covid19app.server.common.Event
import org.covid19app.server.common.EventInfo

data class DeviceNotificationEvent(
        override val eventInfo: EventInfo,
        val deviceId: String,
        val pushNotificationToken: String
) : Event
