package com.bank.transfers.application.infra.http.annotations;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class DocumentValidatorAnnotation implements ConstraintValidator<DocumentValid, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext) {
        if (Strings.isNullOrEmpty(value)) {
            return false;
        }

        final var pattern = Pattern.compile("(^(\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2})|(\\d{14})$)|(^(\\d{3}.\\d{3}.\\d{3}-\\d{2})|(\\d{11})$)", Pattern.CASE_INSENSITIVE);
        final var matcher = pattern.matcher(value);

        return matcher.find();
    }
}
