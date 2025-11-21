package com.eprogs.raiseme.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    @Column(insertable = false)
    private Long lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false)
    private Date lastModifiedDate;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "OTP")
    private String otp;

    @Column(name = "OTP_VALID_UNTIL")
    private Date otpValidUntil;

    @Column(name = "TOKEN", length = 1000)
    private String token;

    @Column(name = "IS_LOCKED", nullable = false)
    private Boolean isLocked = false;

    @Column(name = "IS_FIRST_LOGIN", nullable = false)
    @Builder.Default
    private Boolean isFirstLogin = true;

    @Column(name = "IS_PASSWORD_NEED_CHANGE", nullable = false)
    @Builder.Default
    private Boolean isPasswordNeedChange = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN_AT")
    private Date lastLoginAt;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGOUT_AT")
    private Date lastLogoutAt;

    @Column(name = "FAILED_LOGIN_ATTEMPTS", nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "ACCOUNT_SUSPENDED", nullable = false)
    @Builder.Default
    private Boolean accountSuspended = false;

    @Column(name = "SUSPENSION_REASON")
    private String suspensionReason;
}
