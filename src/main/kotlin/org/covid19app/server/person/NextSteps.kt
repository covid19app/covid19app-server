package org.covid19app.server.person

data class NextSteps(
//        val action: Action,
//        val action: String,
        // Pain text to display.
        val text: String?,
        // Html snippet to display! It allows us to quickly change things by just changing the server.
        val html: String?,
        // External link to open another application.
        val externalLink: String?,
        val externalLinkTitle: String?
)
