package com.mind.core.domain.member

import com.mind.core.domain.base.BaseEntitySimple
import com.mind.core.enums.GenderEnums
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.io.Serial
import java.util.*

@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var email: String,

    var password: String,

    var name: String,

    @Enumerated(EnumType.STRING)
    var gender: GenderEnums

) : BaseEntitySimple() {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", targetEntity = MemberRole::class, cascade = [CascadeType.ALL])
    var memberRole: List<MemberRole>? = mutableListOf()

    companion object {
        @Serial
        private const val serialVersionUID: Long = 2070949215500309276L
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is Member || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = Objects.toString(
        arrayOf(
            Member::id,
            Member::email,
            Member::password,
            Member::name,
            Member::gender
        )
    )
}
