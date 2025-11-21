package com.eprogs.raiseme.dto.authDTOS;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequestDTO(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password) {
}
