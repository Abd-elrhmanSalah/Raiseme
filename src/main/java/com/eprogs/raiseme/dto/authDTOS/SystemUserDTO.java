package com.eprogs.raiseme.dto.authDTOS;

import com.eprogs.raiseme.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserDTO extends BaseDTO<Long> implements Serializable {

    private String email;
    private Boolean isLocked;

}
