package com.eprogs.raiseme.service;

import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.dto.ItemDto;
import com.eprogs.raiseme.entity.Category;
import com.eprogs.raiseme.entity.Item;
import com.eprogs.raiseme.exception.ApplicationException;
import com.eprogs.raiseme.repository.ItemRepository;
import com.eprogs.raiseme.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemService extends BaseService<Item, Long> {

    private final ItemRepository itemRepository;
    private final CategoryService categoryService;


    public ItemDto createItem(ItemDto itemDto) {
        Category categoryExistById = categoryService.isCategoryExistById(itemDto.getCategoryId());

        Item itemMapped = ObjectMapperUtils.map(itemDto, Item.class);
        itemMapped.setCategory(categoryExistById);

        return ObjectMapperUtils.map(itemRepository.save(itemMapped), ItemDto.class);
    }

    public ItemDto updateItem(ItemDto itemDto) {
        categoryService.isCategoryExistById(itemDto.getCategoryId());
        isItemExistById(itemDto.getId());

        Item item = ObjectMapperUtils.map(itemDto, Item.class);

        return ObjectMapperUtils.map(itemRepository.save(item), ItemDto.class);
    }


    public void deleteItemById(Long itemId) {
        Item itemExistById = isItemExistById(itemId);
        itemExistById.setIsLocked(true);
        itemRepository.save(itemExistById);
    }

    public ItemDto getItemById(Long itemId) {
        Item itemExistById = isItemExistById(itemId);
        return ObjectMapperUtils.map(itemExistById, ItemDto.class);
    }

    public List<ItemDto> getAllItems() {
        return ObjectMapperUtils.mapAll(itemRepository.findAllByIsLockedFalse(), ItemDto.class);
    }

    public List<ItemDto> getAllItems(Boolean isBooked) {
        List<Item> items = itemRepository.findAllByIsBooked(isBooked);
        return ObjectMapperUtils.mapAll(items, ItemDto.class);
    }

    public ItemDto bookUnBookItem(Long itemId, boolean booked) {
        Item itemExistById = isItemExistById(itemId);
        itemExistById.setIsBooked(booked);
        return ObjectMapperUtils.map(itemRepository.save(itemExistById), ItemDto.class);
    }

    public List<ItemDto> getItemsByPriceRange(double minPrice, double maxPrice) {
        List<Item> items = itemRepository.findAllByIsLockedFalseAndPriceBetween(minPrice, maxPrice);
        return ObjectMapperUtils.mapAll(items, ItemDto.class);
    }


    public Item isItemExistById(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ApplicationException(new ErrorResponseDTO(
                    HttpStatus.BAD_REQUEST, ERROR_ITEM_NOT_FOUND.getMessageEN(),
                    LocalDateTime.now()));
        }
        return itemRepository.findById(itemId).get();
    }
}
