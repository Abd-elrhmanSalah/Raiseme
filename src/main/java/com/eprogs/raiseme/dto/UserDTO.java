package com.eprogs.raiseme.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String email;
    @JsonIgnore
    private Boolean isLocked;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Long lastModifiedBy;
    private Date createdDate;
    private Date lastModifiedDate;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isNewUser;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActivateAgain;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isChangingEmail;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isChangingBasicInfo;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isChangingPassword;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isOTPSent;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String otp;

}
