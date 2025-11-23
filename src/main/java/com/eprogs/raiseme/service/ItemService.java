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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemService extends BaseService<Item, Long> {

    private final ItemRepository itemRepository;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

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

    /**************************************************************************************/
    // Upload images and add to item
    public ItemDto addImages(Long itemId, List<MultipartFile> images) {
        Item item = isItemExistById(itemId);

        List<String> paths = fileStorageService.uploadItemImages(itemId, images);

        item.getImagePaths().addAll(paths);

        return ObjectMapperUtils.map(itemRepository.save(item), ItemDto.class);
    }

    // Replace all images
    public ItemDto replaceImages(Long itemId, List<MultipartFile> images) {
        Item item = isItemExistById(itemId);

        // delete old images
        item.getImagePaths().forEach(fileStorageService::deleteImage);
        item.setImagePaths(new ArrayList<>());

        // save new images
        List<String> paths = fileStorageService.uploadItemImages(itemId, images);
        item.setImagePaths(paths);

        return ObjectMapperUtils.map(itemRepository.save(item), ItemDto.class);
    }

    // Delete one image
    public ItemDto deleteSingleImage(Long itemId, String imgPath) {
        Item item = isItemExistById(itemId);

        item.getImagePaths().remove(imgPath);
        fileStorageService.deleteImage(imgPath);

        return ObjectMapperUtils.map(itemRepository.save(item), ItemDto.class);
    }

    // Delete all images
    public void deleteAllImages(Long itemId) {
        fileStorageService.deleteAllItemImages(itemId);

        Item item = isItemExistById(itemId);
        item.setImagePaths(new ArrayList<>());
        itemRepository.save(item);
    }
}
