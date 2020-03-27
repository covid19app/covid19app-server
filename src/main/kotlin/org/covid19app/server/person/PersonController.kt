package org.covid19app.server.person

import org.covid19app.server.common.RegistrationStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// TODO: Publish all events to kafka for later use!

@CrossOrigin
@RestController
@RequestMapping("/v1/person")
class PersonController(@Autowired val personRepository: PersonRepository) {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping("/{personId}")
    fun getPerson(@PathVariable personId: String): RegistrationStatus {
        return when (personRepository.existsById(personId)) {
            true -> RegistrationStatus.REGISTERED
            false -> RegistrationStatus.NOT_REGISTERED
        }
    }

    @PostMapping("/{personId}/profile")
    fun postPersonProfile(@PathVariable personId: String, @RequestBody personProfileEvent: PersonProfileEvent): String {
        log.info(">>>> postPersonProfile($personProfileEvent)")
//        assert(personId == personProfileEvent.personId)
        val profileEntity = PersonEntity(personProfileEvent.personId, personProfileEvent.eventInfo.deviceId,
                personProfileEvent.name, personProfileEvent.age, personProfileEvent.sex, personProfileEvent.locale,
                personProfileEvent.deactivated)
        personRepository.save(profileEntity)
        return "\"OK\""
    }

    @PostMapping("/{personId}/travelHistory")
    fun postPersonTravelHistory(
            @PathVariable personId: String, @RequestBody personTravelHistoryEvent: PersonTravelHistoryEvent): String {
        log.info(">>>> postPersonTravelHistory($personTravelHistoryEvent)")
//        assert(personId == personTravelHistoryEvent.personId)
        return "\"OK\""
    }

    @PostMapping("/{personId}/symptoms")
    fun postPersonSymptoms(
            @PathVariable personId: String, @RequestBody personSymptomsEvent: PersonSymptomsEvent): NextSteps {
        log.info(">>>> postPersonSymptoms($personSymptomsEvent)")
//        assert(personId == personSymptomsEvent.personId)
        return if (personSymptomsEvent.feverInCelsius > 37.5) {
            val externalLink = "https://www.google.com/maps/search/?api=1&query=hospital"
            val externalLinkTitle = "Go to Lab!"
            val html = "<a style=\"font-size: 40px;\" href=\"$externalLink\">Go to nearest lab please!</a>"
            NextSteps(null, html, externalLink, externalLinkTitle)
        } else {
            val text = "There is no reason to be worried. Go on with your life. But please be careful!"
//            val html = "<p style=\"font-size: 40px;\">$text</p>"
            NextSteps(text, null, null, null)
        }
    }
}
