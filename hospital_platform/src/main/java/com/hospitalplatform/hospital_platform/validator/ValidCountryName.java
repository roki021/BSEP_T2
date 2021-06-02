package com.hospitalplatform.hospital_platform.validator;

import com.hospitalplatform.hospital_platform.constraint.CountryNameConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CountryNameConstraintValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidCountryName {
    String message() default "Invalid country name";
    Class<?>[] groups() default {};
    Class <? extends Payload>[] payload() default {};
}
