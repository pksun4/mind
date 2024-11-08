package com.mind.core.domain.member

import com.mind.core.domain.base.BaseEntitySimple
import com.mind.core.enums.RoleEnums
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.io.Serial
import java.util.*

@Entity
class MemberRole(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var role: RoleEnums

) : BaseEntitySimple() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_key", foreignKey = ForeignKey(name = "fk_member_role_1"))
    var member: Member? = null

    companion object {
        @Serial
        private const val serialVersionUID: Long = -7365816951704222037L

        fun grantMember(member: Member) = MemberRole(
            role = RoleEnums.MEMBER
        ).apply {
            this.member = member
        }

        fun grantManager(member: Member) = MemberRole(
            role = RoleEnums.MANAGER
        ).apply {
            this.member = member
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is MemberRole || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode() = Objects.hashCode(id)

    override fun toString(): String = Objects.toString(
        arrayOf(
            MemberRole::id,
            MemberRole::role
        )
    )

}
