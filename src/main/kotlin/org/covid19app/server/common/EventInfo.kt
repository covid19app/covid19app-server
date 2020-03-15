package org.covid19app.server.common

data class EventInfo(
        // Event id. Unique random event id used for deduplication.
        val eventId: String,
        // Person id. Random id generated upon initial registration or when person added to the family.
        val personId: String,
        // Device id. Random id generated upon app installation. It is used to deliver push events. Person id itself
        // is not enough because one app installation can have multiple users ("family" tab).
        val deviceId: String,
        // Timestamp generated on device (do all phones have synced clock these days?).
        val timestampInEpochS: Long
)
