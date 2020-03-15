package org.covid19app.server.lab

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class TestEntity(
        @Id
        val testId: String,
        var personId: String?,
        var labResult: LabResult
)

interface TestRepository : JpaRepository<TestEntity, String>
