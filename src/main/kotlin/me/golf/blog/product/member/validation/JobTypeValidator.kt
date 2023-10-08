package me.golf.blog.product.member.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import me.golf.blog.product.member.persist.JobType

class JobTypeValidator : ConstraintValidator<ValidJobType, String> {

    override fun isValid(colorValue: String?, context: ConstraintValidatorContext?): Boolean {
        if (colorValue == null) {
            return true
        }

        return JobType.values().any { it.name == colorValue }
    }
}