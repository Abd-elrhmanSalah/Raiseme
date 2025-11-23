package com.eprogs.raiseme.controller;

import com.eprogs.raiseme.dto.BaseResponse;
import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.dto.ItemDto;
import com.eprogs.raiseme.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.eprogs.raiseme.constant.MessagesEnum.CREATED;
import static com.eprogs.raiseme.constant.MessagesEnum.DELETED;
import static com.eprogs.raiseme.constant.MessagesEnum.UPDATED;

@Tag(
        name = "Item Controller",
        description = " REST APIs for managing Item"
)
@RestController
@RequestMapping(path = "/v1/item", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @Operation(summary = "Create a new item", description = "API to create a new item in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody ItemDto itemDto) {
        itemService.createItem(itemDto);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.CREATED.value(), CREATED.getMessageEN(),
                CREATED.getMessageAR(), HttpStatus.CREATED.getReasonPhrase()),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Update item details", description = "API to update an existing item in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "item successfully updated."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping
    public ResponseEntity<?> updateItem(@Valid @RequestBody ItemDto itemDto) {

        itemService.updateItem(itemDto);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), UPDATED.getMessageEN(),
                UPDATED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);
    }

    @Operation(summary = "Delete Item", description = "API to delete an Item by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item successfully deleted."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItemById(@PathVariable Long itemId) {

        itemService.deleteItemById(itemId);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), DELETED.getMessageEN(),
                DELETED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);

    }

    @Operation(summary = "get Item with item", description = "API to show Item with item in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = " successfully ."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getCItemById(@PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.getItemById(itemId),
                HttpStatus.OK);

    }

    @Operation(summary = "get all Items with item", description = "API to show all non deleted Items in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "successfully ."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(),
                HttpStatus.OK);

    }

    @Operation(summary = "get all Booked/UnBooked Items with item", description = "API to show Booked/UnBooked Items in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "successfully ."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/isBooked/{isBooked}")
    public ResponseEntity<?> getAllItemsBookedUnBooked(
            @Parameter(description = "Only true or false", schema = @Schema(type = "boolean"))
            @PathVariable Boolean isBooked) {
        return new ResponseEntity<>(itemService.getAllItems(isBooked),
                HttpStatus.OK);

    }

    @Operation(summary = "Book a new item", description = "API to book an item in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status successfully"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/book/{itemId}")
    public ResponseEntity<?> bookItem(@PathVariable Long itemId) {
        itemService.bookUnBookItem(itemId, Boolean.TRUE);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), UPDATED.getMessageEN(),
                UPDATED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);
    }

    @Operation(summary = "unBook a new item", description = "API to unBook an item in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status successfully"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/unBook/{itemId}")
    public ResponseEntity<?> unBookItem(@PathVariable Long itemId) {
        itemService.bookUnBookItem(itemId, Boolean.FALSE);
        return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), UPDATED.getMessageEN(),
                UPDATED.getMessageAR(), HttpStatus.OK.getReasonPhrase()),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Get items by price range",
            description = "Returns all items where isLocked = false and price is between the given range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price range provided")
    })
    @GetMapping("/price-range")
    public ResponseEntity<?> getItemsByPriceRange(
            @Parameter(description = "Minimum price", example = "10.0")
            @RequestParam double minPrice,
            @Parameter(description = "Maximum price", example = "100.0")
            @RequestParam double maxPrice) {

        if (minPrice < 0 || maxPrice < 0 || maxPrice < minPrice)
            return ResponseEntity.badRequest().build();


        return ResponseEntity.ok(itemService.getItemsByPriceRange(minPrice, maxPrice));
    }


}
