package me.golf.blog.global.common

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class MinValidator : ConstraintValidator<Min, Long> {

    private var min: Long = 0

    override fun initialize(constraintAnnotation: Min) {
        this.min = constraintAnnotation.min
    }

    override fun isValid(value: Long, context: ConstraintValidatorContext?): Boolean {

        return value >= min
    }
}