package com.eprogs.raiseme.dto.authDTOS;

import com.eprogs.raiseme.validator.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestPasswordDTO {

    @ValidPassword
    private String newPassword;
    private String email;
    private String token;

}
