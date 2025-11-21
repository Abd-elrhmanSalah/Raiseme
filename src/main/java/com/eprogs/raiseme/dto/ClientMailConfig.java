package com.eprogs.raiseme.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientMailConfig {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String email;


}
