package com.pickandeat.api.authentication.dto.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    String message() default "Invalid role. Must be one of: CONSUMER, ADMIN, PRO.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
