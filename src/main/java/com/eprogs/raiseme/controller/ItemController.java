package com.eprogs.raiseme.controller;

import com.eprogs.raiseme.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Item Controller",
        description = " REST APIs for managing Item"
)
@RestController
@RequestMapping(path = "/v1/item", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class ItemController {
    private ItemService itemService;
}
