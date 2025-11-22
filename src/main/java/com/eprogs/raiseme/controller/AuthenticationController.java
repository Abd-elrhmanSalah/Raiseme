package com.eprogs.raiseme.controller;

import com.eprogs.raiseme.constant.MessagesEnum;
import com.eprogs.raiseme.dto.BaseResponse;
import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.dto.UserDTO;
import com.eprogs.raiseme.dto.authDTOS.LoginRequestDTO;
import com.eprogs.raiseme.dto.authDTOS.RestPasswordDTO;
import com.eprogs.raiseme.service.authServices.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.eprogs.raiseme.constant.MessagesEnum.EMAIL_SENT;
import static com.eprogs.raiseme.constant.MessagesEnum.LOGOUT;


@Tag(
        name = "Authentication Controller",
        description = " REST APIs for managing authentication in SCMS"
)
@RestController
@RequestMapping(path = "/v1/authentication", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticateService;

    @Operation(summary = "Register REST API", description = "Register new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@Parameter(name = "registerRequest", required = true)
                                             @RequestBody UserDTO userDTO) {

        return new ResponseEntity<>(authenticateService.register(userDTO), HttpStatus.CREATED);

    }

    @Operation(summary = "Login REST API", description = "Login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status Successes"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Parameter(name = "loginRequest", required = true)
                                   @RequestBody LoginRequestDTO loginRequest) {

        return new ResponseEntity<>(authenticateService.authenticate(loginRequest), HttpStatus.OK);

    }

    @Operation(summary = "LogOut REST API", description = "Logout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status Successes"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        authenticateService.logout(request.getHeader("Authorization"));
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), LOGOUT.getMessageEN(),
                LOGOUT.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }

    @Operation(summary = "Send OTP RESTAPI", description = "Send OTP")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status Successes"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/sendOTP")
    public ResponseEntity<?> sendOTP(@Parameter(name = "email", required = true)
                                     @RequestParam String email) {

        authenticateService.sendOTP(email.toLowerCase());
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), EMAIL_SENT.getMessageEN(),
                EMAIL_SENT.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }

    @Operation(summary = "verifyOTP for User REST API", description = "activate user that have first loging by verifyOTP")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status Successes"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/verifyOTP")
    public ResponseEntity<?> verifyOTP(@Parameter(name = "email", required = true) @RequestParam String email,
                                       @Parameter(name = "otp", required = true) @RequestParam String otp, HttpServletRequest request) {

        return new ResponseEntity<>(authenticateService.verifyOTP(email.toLowerCase(), otp, request),
                HttpStatus.OK);

    }

    @Operation(summary = "RestPassword RESTAPI", description = "RestPassword")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status Successes"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/restPassword")
    public ResponseEntity<?> restPassword(@RequestParam @Parameter(description = "email") String email) {

        authenticateService.restPassword(email);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), EMAIL_SENT.getMessageEN(),
                EMAIL_SENT.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }


    @Operation(summary = "Change Password RESTAPI", description = "Verify user to change password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status Successes"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Parameter(schema = @Schema(implementation = RestPasswordDTO.class))
                                            @Valid @RequestBody RestPasswordDTO restPasswordDTO) {

        authenticateService.changeUserPassword(restPasswordDTO);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessagesEnum.UPDATED.getMessageEN(),
                MessagesEnum.UPDATED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }

    @Operation(summary = "Get user by id RESTAPI", description = "get user data by id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserData(@PathVariable Long userId) {

        return new ResponseEntity<>(authenticateService.getUserDataById(userId), HttpStatus.OK);

    }

    @Operation(summary = "Lock an user", description = "API to lock an user by email.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully locked."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/lock")
    public ResponseEntity<?> lockUserByEmail(@RequestParam @Parameter(description = "email") String email) {

        authenticateService.lockUserByEmail(email);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessagesEnum.UPDATED.getMessageEN(),
                MessagesEnum.UPDATED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }

    @Operation(summary = "unLock an user", description = "API to unLockan user by email.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully unLocked."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/unLock")
    public ResponseEntity<?> unLockUserByEmail(@RequestParam @Parameter(description = "email") String email) {

        authenticateService.unLockUserByEmail(email);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessagesEnum.UPDATED.getMessageEN(),
                MessagesEnum.UPDATED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }
}

