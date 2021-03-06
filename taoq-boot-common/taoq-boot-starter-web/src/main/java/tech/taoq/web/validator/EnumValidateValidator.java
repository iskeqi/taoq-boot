package tech.taoq.web.validator;


import tech.taoq.web.validator.annotation.EnumValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * EnumValidateValidator
 *
 * @author keqi
 */
public class EnumValidateValidator implements ConstraintValidator<EnumValidate, String> {

    private Class<?> clazz;

    @Override
    public void initialize(EnumValidate constraintAnnotation) {
        clazz = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (Object constant : clazz.getEnumConstants()) {
            BaseEnumValidate baseEnumValidate = (BaseEnumValidate) constant;
            if (baseEnumValidate.existCode(value)) {
                return true;
            }
        }

        return false;
    }
}
