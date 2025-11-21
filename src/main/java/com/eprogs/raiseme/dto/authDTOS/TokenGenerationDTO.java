package com.eprogs.raiseme.dto.authDTOS;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenGenerationDTO {

    private SystemUserDetails userDetails;
    private Long userId;
    private String email;

}