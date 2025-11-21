package com.eprogs.raiseme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class ErrorResponseDTO {

    private HttpStatus errorCode;
    private String errorMessage;
    private String errorDescription;
    private LocalDateTime errorTime;

    public ErrorResponseDTO(HttpStatus errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponseDTO(HttpStatus errorCode, String errorMessage, LocalDateTime errorTime) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorTime = errorTime;
    }

    public ErrorResponseDTO(HttpStatus errorCode, String errorMessage, String errorDescription) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDescription = errorDescription;
    }


}
