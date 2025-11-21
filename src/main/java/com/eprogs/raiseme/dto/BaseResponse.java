package com.eprogs.raiseme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {

    private Integer statusCode;
    private String messageEN;
    private String messageAR;
    private T data;

    public BaseResponse(int statusCode, String messageEN) {
        this.statusCode = statusCode;
        this.messageEN = messageEN;

    }


}
