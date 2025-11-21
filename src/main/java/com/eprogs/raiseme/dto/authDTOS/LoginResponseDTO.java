package com.eprogs.raiseme.dto.authDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private HttpStatus status;
    private String jwtToken;

}