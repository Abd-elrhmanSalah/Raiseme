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
public class ItemDto {

    private Long id;
    private String title;
    private String description;
    private double price;
    private Long categoryId;
    private List<String> imagePaths;

}
