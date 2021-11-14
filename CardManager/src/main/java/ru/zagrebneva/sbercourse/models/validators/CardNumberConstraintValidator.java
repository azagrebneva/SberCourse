package ru.zagrebneva.sbercourse.models.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CardNumberConstraintValidator implements ConstraintValidator<CardNumberConstraint, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!((s.length() == 16)||(s.length() == 18))) {
           return false;
        }
        String regex = "[0-9]+";
        return s.matches(regex);
    }
}