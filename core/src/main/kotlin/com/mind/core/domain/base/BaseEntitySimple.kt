package com.mind.core.domain.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntitySimple : Serializable {

    companion object {
        @Serial
        private const val serialVersionUID: Long = 635159468016099952L
    }

    @Column(nullable = false)
    var isDeleted: Boolean? = false

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now()

}
