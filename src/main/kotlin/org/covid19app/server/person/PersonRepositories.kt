package org.covid19app.server.person

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class PersonEntity(
        @Id
        val personId: String,
        val deviceId: String,
        val age: Int,
        val sex: Sex,
        val name: String,
        val deleted: Boolean
)

interface PersonRepository : JpaRepository<PersonEntity, String>
