package org.covid19app.server.lab

import org.covid19app.server.common.Event
import org.covid19app.server.common.EventInfo

data class TestPairEvent(
        override val eventInfo: EventInfo,
        val testId: String,
        val personId: String
): Event

data class TestResultEvent(
        override val eventInfo: EventInfo,
        val testId: String,
        val labResult: LabResult
): Event
