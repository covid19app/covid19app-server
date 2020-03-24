package org.covid19app.server

import org.covid19app.server.common.EventInfo
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

private val idCounter = AtomicLong(0)

fun freshId(prefix: String): String {
    return "${prefix}:${idCounter.incrementAndGet()}"
}

fun freshEventInfo(deviceId: String): EventInfo {
    return EventInfo(freshId("event"), deviceId, Instant.now().epochSecond)
}

fun freshEventInfo(): EventInfo {
    return freshEventInfo(freshId("device"))
}
