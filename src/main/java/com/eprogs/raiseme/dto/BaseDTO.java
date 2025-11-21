package com.eprogs.raiseme.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
public abstract class BaseDTO<T extends Number> {

    private T id;

    private Long createdBy;

    private Long lastModifiedBy;

    private Date createdDate;

    private Date lastModifiedDate;

}
