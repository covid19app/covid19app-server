package org.covid19app.server.lab

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

// TODO: Publish all events to kafka for later use!

@CrossOrigin
@RestController
@RequestMapping("/v1/test")
class TestController(@Autowired val testRepository: TestRepository) {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping("/{testId}")
    fun getTest(@PathVariable testId: String): LabResult {
        return testRepository.findByIdOrNull(testId)?.labResult ?: LabResult.UNKNOWN
    }

    @PostMapping("/{testId}/pair")
    fun postTestPair(@PathVariable testId: String, @RequestBody testPairEvent: TestPairEvent): String {
        log.info(">>>> postTestPair($testPairEvent)")
        val testEntity = TestEntity(testId, testPairEvent.personId, LabResult.IN_PROGRESS)
        testRepository.save(testEntity)
        return "\"OK\""
    }

    @PostMapping("/{testId}/result")
    fun postTestResult(@PathVariable testId: String, @RequestBody testResultEvent: TestResultEvent): String {
        log.info(">>>> postTestResult($testResultEvent)")
        return when (val testEntity = testRepository.findByIdOrNull(testId)) {
            null -> "\"ERROR: testId = $testId not is not paired yet. Please scan again.\""
            else -> {
                testEntity.labResult = testResultEvent.labResult
                testRepository.save(testEntity)
                "\"OK\""
            }
        }
    }
}
