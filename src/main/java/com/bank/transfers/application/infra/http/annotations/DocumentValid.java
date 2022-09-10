package com.bank.transfers.application.infra.http.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DocumentValidatorAnnotation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DocumentValid {
    public String message() default "O campo do documento deve ser valido";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
