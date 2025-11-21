package com.eprogs.raiseme.controller;

import com.eprogs.raiseme.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Category Controller",
        description = " REST APIs for managing category"
)
@RestController
@RequestMapping(path = "/v1/category", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class CategoryController {
    private CategoryService categoryService;
}
