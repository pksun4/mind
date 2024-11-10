package com.mind.core.domain.board

import com.mind.core.domain.base.BaseEntity
import com.mind.core.domain.base.BaseEntitySimple
import com.mind.core.domain.member.Member
import com.mind.core.enums.BoardEnums
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.io.Serial
import java.util.Objects
import org.springframework.data.annotation.CreatedBy

@Entity
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var type: BoardEnums,

    var title: String,

    var content: String?,

) : BaseEntity() {

    var views: Int = 0

    companion object {
        @Serial
        private const val serialVersionUID: Long = 812849501568560935L
    }

    fun read() {
        views = views.plus(1)
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is Board || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun toString(): String = Objects.toString(
        arrayOf(
            Board::id,
            Board::type,
            Board::title,
            Board::content,
            Board::views
        )
    )
}
