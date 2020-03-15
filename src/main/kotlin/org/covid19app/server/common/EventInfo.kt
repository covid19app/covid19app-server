package org.covid19app.server.common

import java.time.Instant

data class EventInfo(
        // Unique random event id used for deduplication.
        val id: String,
        // Person id. Random id generated upon initial registration or when person added to the family.
        val personId: String,
        // Device id. Random id generated upon app installation. It is used to deliver push events. Person id itself
        // is not enough because one app installation can have multiple users ("family" tab).
        val deviceId: String,
        // Timestamp generated on device (do all phones have synced clock these days?).
        val timestamp: Instant
)