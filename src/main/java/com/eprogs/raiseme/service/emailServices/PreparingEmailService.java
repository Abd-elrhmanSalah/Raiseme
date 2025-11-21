package com.eprogs.raiseme.service.emailServices;

import com.eprogs.raiseme.constant.EmailTemplateConstant;
import com.eprogs.raiseme.dto.EmailDetails;
import com.eprogs.raiseme.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class PreparingEmailService {

    @Value("${base.url}")
    private String baseUrl;

    private final EmailService emailService;

    public void prepareAndSendEmail(UserDTO userDTO) {
        if (userDTO.getIsOTPSent() != null && userDTO.getIsOTPSent() && userDTO.getIsNewUser()) {
            prepareOTPAndSendEmail(userDTO);

        } else if (userDTO.getIsChangingPassword()) {
            prepareRestPasswordAndSendEmail(userDTO);
        }

        if (userDTO.getIsChangingEmail())
            prepareChangeEmailDataAndSendEmail(userDTO);
        else if (userDTO.getIsActivateAgain())
            prepareActivateAccountDataAndSendEmail(userDTO);
        else
            prepareDeactivateAccountDataAndSendEmail(userDTO);

    }

    private void prepareRestPasswordAndSendEmail(UserDTO userDTO) {

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userDTO.getEmail())
                .variables(Map.of(
                        "username", userDTO.getFirstName(),
                        "newPassword", userDTO.getPassword()
                ))
                .build();
        emailService.sendHTMLMailWithClientSMTPCred(emailDetails, EmailTemplateConstant.REST_ACCOUNT_PASSWORD);
    }

    private void prepareDeactivateAccountDataAndSendEmail(UserDTO userDTO) {

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userDTO.getEmail())
                .variables(Map.of(
                        "username", userDTO.getFirstName()
                ))
                .build();
        emailService.sendHTMLMailWithClientSMTPCred(emailDetails, EmailTemplateConstant.DEACTIVATE_ACCOUNT);
    }

    private void prepareActivateAccountDataAndSendEmail(UserDTO userDTO) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userDTO.getEmail())
                .variables(Map.of(
                        "username", userDTO.getFirstName()

                ))
                .build();
        emailService.sendHTMLMailWithClientSMTPCred(emailDetails, EmailTemplateConstant.ACTIVATE_ACCOUNT);
    }

    private void prepareChangeEmailDataAndSendEmail(UserDTO userDTO) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userDTO.getEmail())
                .build();
        emailService.sendHTMLMailWithClientSMTPCred(emailDetails, EmailTemplateConstant.ACTIVATE_ACCOUNT);
    }


    private void prepareOTPAndSendEmail(UserDTO userDTO) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userDTO.getEmail())
                .variables(Map.of(
                        "username", userDTO.getFirstName(),
                        "otpCode", userDTO.getOtp()
                ))
                .build();
        emailService.sendHTMLMailWithClientSMTPCred(emailDetails, EmailTemplateConstant.OTP);
    }


}
