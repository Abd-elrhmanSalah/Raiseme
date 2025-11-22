package com.eprogs.raiseme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponseDto {
    private Long id;
    private String title;
    private List<ItemResponseDto> items;
    private Boolean isLocked;
}
