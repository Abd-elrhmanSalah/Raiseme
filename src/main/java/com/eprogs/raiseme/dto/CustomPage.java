package com.eprogs.raiseme.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class CustomPage<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private boolean isFirst;
    private Long totalRecordsInDb;

    public CustomPage(Page<T> page, Long totalRecordsInDb) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.isLast = page.isLast();
        this.isFirst = page.isFirst();
        this.totalRecordsInDb = totalRecordsInDb;
    }

    public CustomPage(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean isLast, boolean isFirst, Long totalRecordsInDb) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
        this.isFirst = isFirst;
        this.totalRecordsInDb = totalRecordsInDb;
    }
}
