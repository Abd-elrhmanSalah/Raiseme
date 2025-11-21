package com.eprogs.raiseme.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailTemplateConstant {

    DEACTIVATE_ACCOUNT("Your Raiseme Has Been Deactivated", "deactivate_account.html"),
    ACTIVATE_ACCOUNT("Your Raiseme Account Has Been Reactivated", "activate_account.html"),
    REST_ACCOUNT_PASSWORD("Your Raiseme Account Password Has Been Rest", "resetPassword.html"),

    OTP("Your One-Time Password (OTP) for Ecommerce", "otp.html"),
    CONTACT_FORM("New Contact Form Submission for Jodayn Compliance Management Platform", "contact_form.html"),
    ;
    private final String subject;
    private final String templateFileName;
}

