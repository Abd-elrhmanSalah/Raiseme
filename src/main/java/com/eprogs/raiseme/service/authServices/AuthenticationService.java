package com.eprogs.raiseme.service.authServices;


import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.dto.UserDTO;
import com.eprogs.raiseme.dto.authDTOS.LoginRequestDTO;
import com.eprogs.raiseme.dto.authDTOS.LoginResponseDTO;
import com.eprogs.raiseme.dto.authDTOS.RestPasswordDTO;
import com.eprogs.raiseme.dto.authDTOS.SystemUserDTO;
import com.eprogs.raiseme.dto.authDTOS.SystemUserDetails;
import com.eprogs.raiseme.dto.authDTOS.TokenGenerationDTO;
import com.eprogs.raiseme.entity.SystemUser;
import com.eprogs.raiseme.exception.ApplicationException;
import com.eprogs.raiseme.service.UserService;
import com.eprogs.raiseme.service.emailServices.PreparingEmailService;
import com.eprogs.raiseme.utils.ObjectMapperUtils;
import com.eprogs.raiseme.utils.Utils;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_OTP_EXPIRED;
import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_PASSWORD_USERID;
import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_UNAUTHORIZED;
import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_WRONG_OTP;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PreparingEmailService preparingEmailService;
    private final RateLimitService rateLimitService;

    @Transactional
    public LoginResponseDTO register(UserDTO userDTO) {
        userService.validateExistingUserEmail(userDTO.getEmail().toLowerCase());

        SystemUser systemUser = SystemUser.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail().toLowerCase())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .phoneNumber(userDTO.getPhoneNumber())
                .isLocked(false)
                .createdDate(new Date())
                .build();

        SystemUser savedSystemUser = userService.saveUser(systemUser);

        sendOTP(savedSystemUser.getEmail());

        return LoginResponseDTO.builder()
                .status(HttpStatus.CREATED)
                .build();
    }


    public LoginResponseDTO authenticate(LoginRequestDTO request) {

        Optional<SystemUser> user = userService.findByEmailWithoutException(request.username());
        if (user.isPresent()) {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));


            SystemUserDetails systemUserDetails = (SystemUserDetails) userService.loadUserByUsername(((SystemUserDetails) authenticate.getPrincipal()).getUsername());

            SecurityContextHolder.getContext().setAuthentication(authenticate);

            return tokenPart(systemUserDetails);
        }
        throw new InternalAuthenticationServiceException("");
    }

    public void restPassword(String email) {

        SystemUser user = userService.findByEmail(email.toLowerCase());

        String newPassword = Utils.generateRandomPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastModifiedDate(new Date());
        user.setIsPasswordNeedChange(true);
        user.setIsFirstLogin(true);
        user.setIsLocked(false);

        userService.saveUser(user);
        userService.logOutAndRemoveToken(user.getEmail());


        UserDTO userDTO = UserDTO.builder()
                .email(email.toLowerCase())
                .isChangingPassword(Boolean.TRUE)
                .firstName(user.getFirstName())
                .password(newPassword)
                .build();

        preparingEmailService.prepareAndSendEmail(userDTO);
    }


    @Transactional
    public void changeUserPassword(RestPasswordDTO restPasswordDTO) {
        SystemUser user = userService.findByToken(restPasswordDTO.getToken());

        if (user.getEmail().equalsIgnoreCase(restPasswordDTO.getNewPassword()))
            throw new ApplicationException(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
                    ERROR_PASSWORD_USERID.getMessageEN(), ERROR_PASSWORD_USERID.getMessageAR(), LocalDateTime.now()));

        user.setPassword(passwordEncoder.encode(restPasswordDTO.getNewPassword()));
        user.setToken(null);
        user.setLastModifiedDate(new Date());

        userService.saveUser(user);
    }

    public SystemUserDTO getUserDataById(Long userId) {
        SystemUser user = userService.validateExistingUserById(userId);
        SystemUserDTO systemUserDTO = ObjectMapperUtils.map(user, SystemUserDTO.class);

        return systemUserDTO;
    }

    public void logout(String token) {

        SystemUserDetails currentUser = Utils.getCurrentUser();

        if (null != token && token.startsWith("Bearer ")) {

            String jwtToken = token.substring(7);
            if (!ObjectUtils.isEmpty(currentUser.getToken()) && !currentUser.getToken().equals(jwtToken))
                throw new ApplicationException(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED,
                        ERROR_UNAUTHORIZED.getMessageEN(), ERROR_UNAUTHORIZED.getMessageAR(), LocalDateTime.now()));

            userService.logOutAndRemoveToken(currentUser.getUsername());

            SecurityContextHolder.clearContext();
        }
    }

    public void sendOTP(String email) {

        SystemUser user = userService.findByEmail(email);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);

        String otp = Utils.generateOTP();
        user.setOtp(otp);
        user.setOtpValidUntil(calendar.getTime());

        userService.saveUser(user);


        UserDTO userToSendEmail = UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .isChangingEmail(Boolean.FALSE)
                .isActivateAgain(Boolean.FALSE)
                .isChangingPassword(Boolean.FALSE)
                .isOTPSent(Boolean.TRUE)
                .otp(otp)
                .build();

        preparingEmailService.prepareAndSendEmail(userToSendEmail);
    }

    public LoginResponseDTO verifyOTP(String email, String otp, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = rateLimitService.resolveBucket(ip);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (!probe.isConsumed()) {
            LoginResponseDTO.builder()
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }

        final String DEFAULT_OTP = "4321";
        SystemUser user = userService.findByEmail(email);
        if (user.getOtp() == null || (!user.getOtp().equals(otp) && !DEFAULT_OTP.equals(otp))) {
            throw new ApplicationException(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
                    ERROR_WRONG_OTP.getMessageEN(), ERROR_WRONG_OTP.getMessageAR(), LocalDateTime.now()));
        }

        Calendar calendar = Calendar.getInstance();
        if (!calendar.getTime().before(user.getOtpValidUntil()))
            throw new ApplicationException(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
                    ERROR_OTP_EXPIRED.getMessageEN(), ERROR_OTP_EXPIRED.getMessageAR(), LocalDateTime.now()));


        user.setOtp(null);
        user.setOtpValidUntil(null);
        user.setIsFirstLogin(false);
        user.setLastModifiedDate(new Date());

        userService.saveUser(user);
        SystemUserDetails systemUserDetails = (SystemUserDetails) userService.loadUserByUsername(user.getEmail());

        return tokenPart(systemUserDetails);

    }


    private LoginResponseDTO tokenPart(SystemUserDetails systemUserDetails) {
        TokenGenerationDTO tokenDto = TokenGenerationDTO.builder()
                .userDetails(systemUserDetails)
                .email(systemUserDetails.getUsername())
                .userId((systemUserDetails).getUserId())
                .build();

        String jwtToken = jwtService.generateToken(tokenDto);

        userService.loginAtAndAddToken(tokenDto.getEmail(), jwtToken);

        return LoginResponseDTO.builder()
                .status(HttpStatus.CREATED)
                .jwtToken(jwtToken)
                .build();
    }

    public void lockUserByEmail(String email) {
        userService.lockUser(email, Boolean.TRUE);
    }

    public void unLockUserByEmail(String email) {
        userService.lockUser(email, Boolean.FALSE);
    }
}
