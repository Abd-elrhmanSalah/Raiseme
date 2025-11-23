package com.eprogs.raiseme.controller;

import com.eprogs.raiseme.dto.BaseResponse;
import com.eprogs.raiseme.dto.CategoryRequestDto;
import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.eprogs.raiseme.constant.MessagesEnum.CREATED;
import static com.eprogs.raiseme.constant.MessagesEnum.DELETED;
import static com.eprogs.raiseme.constant.MessagesEnum.UPDATED;

@Tag(
        name = "Category Controller",
        description = " REST APIs for managing category"
)
@RestController
@RequestMapping(path = "/v1/category", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category", description = "API to create a new category in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDto CategoryDto) {
        categoryService.createCategory(CategoryDto);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.CREATED.value(), CREATED.getMessageEN(),
                CREATED.getMessageAR(), HttpStatus.CREATED.getReasonPhrase()),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Update category details", description = "API to update an existing category in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category successfully updated."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId,
                                            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        categoryService.updateCategory(categoryId, categoryRequestDto);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), UPDATED.getMessageEN(),
                UPDATED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);
    }

    @Operation(summary = "Delete category", description = "API to delete an category by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category successfully deleted."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long categoryId) {

        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), DELETED.getMessageEN(),
                DELETED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }

    @Operation(summary = "get category with item", description = "API to show category with item in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = " successfully ."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryWithItems(@PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.getCategoryWithItems(categoryId),
                HttpStatus.OK);

    }

    @Operation(summary = "get all categories with item", description = "API to show all categories with item in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = " successfully ."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllCategoriesWithItems() {
        return new ResponseEntity<>(categoryService.getAllCategories(),
                HttpStatus.OK);

    }
}
