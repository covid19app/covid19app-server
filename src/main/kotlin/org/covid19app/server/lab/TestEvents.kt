package org.covid19app.server.lab

import org.covid19app.server.common.EventInfo

data class TestPairEvent(
        val eventInfo: EventInfo,
        val testId: String,
        val personId: String
)

data class TestResultEvent(
        val eventInfo: EventInfo,
        val testId: String,
        val labResult: LabResult
)
