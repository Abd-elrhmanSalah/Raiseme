package com.eprogs.raiseme.validator;

import com.eprogs.raiseme.config.PasswordPolicyConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Autowired
    private PasswordPolicyConfig passwordPolicyConfig;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_\\-+={}\\[\\]|:;\"'<>,.?/";


    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            buildConstraintViolation(context, "Password cannot be null");
            return false;
        }

        return validateLength(password, context) &&
                validateCharacterComposition(password, context) &&
                validateRepeatedCharacters(password, context);
    }

    private boolean validateLength(String password, ConstraintValidatorContext context) {
        int length = password.length();
        if (length < passwordPolicyConfig.getMinLength() || length > passwordPolicyConfig.getMaxLength()) {
            buildConstraintViolation(context,
                    String.format("Password must be between %d and %d characters",
                            passwordPolicyConfig.getMinLength(),
                            passwordPolicyConfig.getMaxLength()));
            return false;
        }
        return true;
    }

    private boolean validateCharacterComposition(String password, ConstraintValidatorContext context) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (passwordPolicyConfig.isRequireUppercase() && Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (passwordPolicyConfig.isRequireLowercase() && Character.isLowerCase(c)) {
                hasLowercase = true;
            }
            if (passwordPolicyConfig.isRequireNumber() && Character.isDigit(c)) {
                hasNumber = true;
            }
            if (passwordPolicyConfig.isRequireSpecialChar() && SPECIAL_CHARS.indexOf(c) != -1) {
                hasSpecialChar = true;
            }
        }

        List<String> missingRequirements = new ArrayList<>();
        if (passwordPolicyConfig.isRequireUppercase() && !hasUppercase) {
            missingRequirements.add("uppercase letter");
        }
        if (passwordPolicyConfig.isRequireLowercase() && !hasLowercase) {
            missingRequirements.add("lowercase letter");
        }
        if (passwordPolicyConfig.isRequireNumber() && !hasNumber) {
            missingRequirements.add("number");
        }
        if (passwordPolicyConfig.isRequireSpecialChar() && !hasSpecialChar) {
            missingRequirements.add("special character");
        }

        if (!missingRequirements.isEmpty()) {
            buildConstraintViolation(context,
                    "Password must contain at least one " + String.join(", ", missingRequirements));
            return false;
        }
        return true;
    }

    private boolean validateRepeatedCharacters(String password, ConstraintValidatorContext context) {
        int repeatCount = 1;
        char previousChar = '\0';

        for (char c : password.toCharArray()) {
            if (c == previousChar) {
                repeatCount++;
                if (repeatCount > 3) {
                    buildConstraintViolation(context,
                            "Password cannot contain more than 3 identical characters in sequence");
                    return false;
                }
            } else {
                repeatCount = 1;
                previousChar = c;
            }
        }
        return true;
    }

    private void buildConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
