package ru.zagrebneva.sbercourse.models.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=CardNumberConstraintValidator.class)
public @interface CardNumberConstraint {
    String message() default "Card number consists of 16 or 18 numbers";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
