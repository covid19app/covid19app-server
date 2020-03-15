package org.covid19app.server

import org.covid19app.server.common.EventInfo
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

private val idCounter = AtomicLong(0)

fun freshId(prefix: String): String {
    return "${prefix}${idCounter.incrementAndGet()}"
}

fun freshEventInfo(personId: String, deviceId: String): EventInfo {
    return EventInfo(freshId("event"), personId, deviceId, Instant.now().epochSecond)
}

fun freshEventInfo(): EventInfo {
    return freshEventInfo(freshId("person"), freshId("device"))
}

fun freshEventInfo(eventInfo: EventInfo): EventInfo {
    return freshEventInfo(eventInfo.personId, eventInfo.deviceId)
}
