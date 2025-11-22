package com.eprogs.raiseme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemRequestDto {
    private String title;
    private String description;
    private double price;
    private String imagePath;
    private Long categoryId;

}
