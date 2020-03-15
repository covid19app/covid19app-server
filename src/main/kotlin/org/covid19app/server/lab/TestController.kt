package org.covid19app.server.lab

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

// TODO: Publish all events to kafka for later use!

@RestController
@RequestMapping("/v1/test")
class TestController(@Autowired val testRepository: TestRepository) {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping("/{testId}/pair")
    fun postPair(@PathVariable testId: String, @RequestBody pairEvent: TestPairEvent): String {
        log.info(">>>> postPairEvent($pairEvent)")
        val testEntity = TestEntity(testId, pairEvent.personId, LabResult.IN_PROGRESS)
        testRepository.save(testEntity)
        return "OK"
    }

    @GetMapping("/{testId}/result")
    fun getResult(@PathVariable testId: String): LabResult {
        return testRepository.findByIdOrNull(testId)?.labResult ?: LabResult.UNKNOWN
    }

    @PostMapping("/{testId}/result")
    fun postResult(@PathVariable testId: String, @RequestBody resultEvent: TestResultEvent): String {
        log.info(">>>> postResult($resultEvent)")
        return when (val testEntity = testRepository.findByIdOrNull(testId)) {
            null -> "ERROR: testId = $testId not is not paired yet. Please scan again."
            else -> {
                testEntity.labResult = resultEvent.labResult
                testRepository.save(testEntity)
                "OK"
            }
        }
    }
}
