package com.mind.core.domain.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity : Serializable {

    companion object {
        @Serial
        private const val serialVersionUID: Long = 4313442928713917492L
    }

    @Column(nullable = false)
    var isDeleted: Boolean? = false

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: Long? = null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @LastModifiedBy
    var updatedBy: Long? = null

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now()

}
