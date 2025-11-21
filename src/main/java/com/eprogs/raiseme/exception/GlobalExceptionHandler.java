package com.eprogs.raiseme.exception;

import com.eprogs.raiseme.dto.BaseResponse;
import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_AUTHENTICATION_FAILED;
import static com.eprogs.raiseme.constant.MessagesEnum.FORBIDDEN;


@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final UserService userService;


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {

        logError(ex.getMessage(), ex);
        if (ex instanceof InternalAuthenticationServiceException) {
            if (ex.getCause() instanceof ApplicationException)
                return new ResponseEntity<>(((ApplicationException) ex.getCause()).getErrorResponseDTO(), HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .errorMessage(ex.getMessage())
                        .errorDescription(request.getDescription(true))
                        .errorTime(LocalDateTime.now())
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationException ex, WebRequest request) {

        logError(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.getErrorResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingRequestParam(MissingServletRequestParameterException ex, WebRequest request) {
        logError(ex.getMessage(), ex);

        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .errorCode(HttpStatus.BAD_REQUEST)
                        .errorMessage("Missing required parameter: " + ex.getParameterName())
                        .errorDescription("Required input was not provided")
                        .errorTime(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingMultipartPart(MissingServletRequestPartException ex) {
        logError("Missing multipart field: " + ex.getRequestPartName(), ex);

        return new ResponseEntity<>(
                ErrorResponseDTO.builder()
                        .errorCode(HttpStatus.BAD_REQUEST)
                        .errorMessage("Missing required file: " + ex.getRequestPartName())
                        .errorDescription("Multipart field '" + ex.getRequestPartName() + "' is required but was not provided.")
                        .errorTime(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {

        logError(ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .messageEN("Validation Error")
                        .messageAR("بيانات غير صحيحة")
                        .data(errors)
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {

        logError(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.FORBIDDEN.value(), FORBIDDEN.getMessageEN(),
                FORBIDDEN.getMessageAR(), HttpStatus.FORBIDDEN.getReasonPhrase()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> handleBadCredentialsException(Exception ex, HttpServletRequest request, WebRequest request2) {

        logError(ex.getMessage(), ex);

        if (ex instanceof BadCredentialsException) {
            userService.findByEmailWithoutException(extractUsernameFromCachedRequest(request));
        }

        return new ResponseEntity<>(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
                ERROR_AUTHENTICATION_FAILED.getMessageEN(), ERROR_AUTHENTICATION_FAILED.getMessageAR()),
                HttpStatus.BAD_REQUEST);
    }


    private void logError(String message, Exception ex) {
        log.error(message, ex);
        ex.printStackTrace();
    }

    private String extractUsernameFromCachedRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] content = wrapper.getContentAsByteArray();
            if (content != null && content.length > 0) {
                try {
                    JsonNode jsonNode = new ObjectMapper().readTree(content);
                    return jsonNode.path("username").asText();
                } catch (IOException e) {
                    log.warn("Failed to parse JSON payload", e);
                }
            }
        }
        return null;
    }


}