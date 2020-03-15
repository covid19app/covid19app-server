package org.covid19app.server.person

data class NextSteps(
        val action: Action,
        // Details is html snippet to display! It allows us to quickly change things by just changing the server.
        val details: String?
)
