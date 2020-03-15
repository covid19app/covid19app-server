package org.covid19app.server.person

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ProfileEntity(
        @Id
        val personId: String,
        val age: Int,
        val sex: Sex,
        val name: String,
        val deleted: Boolean
)

interface ProfileRepository : JpaRepository<ProfileEntity, String>
