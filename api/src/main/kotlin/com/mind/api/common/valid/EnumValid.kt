package com.mind.api.common.valid

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValidEnumValidator::class])
annotation class EnumValid(

    val message: String = "invalid enum value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val enumClass: KClass<out Enum<*>>

)

class ValidEnumValidator : ConstraintValidator<EnumValid, Any> {
    private lateinit var enumValues: Array<out Enum<*>>

    override fun initialize(annotion: EnumValid) {
        enumValues = annotion.enumClass.java.enumConstants
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }

        return enumValues.any { it.name == value.toString() }
    }
}
