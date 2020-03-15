package org.covid19app.server.person

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// TODO: Publish all events to kafka for later use!

@RestController
@RequestMapping("/v1/person")
class PersonController(@Autowired val profileRepository: ProfileRepository) {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping("/{personId}")
    fun getPerson(@PathVariable personId: String): String {
        return when (profileRepository.existsById(personId)) {
            true -> "\"REGISTERED\""
            false -> "\"NOT REGISTERED\""
        }
    }

    @PutMapping("/{personId}/profile")
    fun putProfile(@PathVariable personId: String, @RequestBody profileEvent: PersonProfileEvent) {
        log.info(">>>> putProfile($profileEvent)")
        val profileEntity = ProfileEntity(
                personId, profileEvent.age, profileEvent.sex, profileEvent.name, profileEvent.deleted)
        profileRepository.save(profileEntity)
    }

    @PutMapping("/{personId}/travelHistory")
    fun putTravelHistory(@PathVariable personId: String, @RequestBody travelHistoryEvent: PersonTravelHistoryEvent) {
        log.info(">>>> putTravelHistory($travelHistoryEvent)")
    }

    @PostMapping("/{personId}/symptoms")
    fun postSymptoms(@PathVariable personId: String, @RequestBody symptomsEvent: PersonSymptomsEvent): NextSteps {
        log.info(">>>> postSymptoms($symptomsEvent)")
        return if (symptomsEvent.feverInCelsius > 37.5) {
            val labHref = "https://www.google.com/maps/search/?api=1&query=hospital"
            NextSteps(Action.GET_TESTED, "<a href=\"$labHref\">Go to nearest lab please!</a>")
        } else {
            NextSteps(Action.STAY_HEALTHY, "There is no reason to be worried. Go on with your life but be careful!")
        }
    }
}
