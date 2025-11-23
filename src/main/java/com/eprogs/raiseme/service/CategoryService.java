package com.eprogs.raiseme.service;

import com.eprogs.raiseme.dto.CategoryRequestDto;
import com.eprogs.raiseme.dto.CategoryResponseDto;
import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.entity.Category;
import com.eprogs.raiseme.entity.Item;
import com.eprogs.raiseme.exception.ApplicationException;
import com.eprogs.raiseme.repository.CategoryRepository;
import com.eprogs.raiseme.repository.ItemRepository;
import com.eprogs.raiseme.utils.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_CATEGORY_TITLE_EXIST;
import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_ITEM_NOT_FOUND;

@Service
@AllArgsConstructor
public class CategoryService extends BaseService<Category, Long> {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public CategoryResponseDto createCategory(CategoryRequestDto categoryDto) {
        isCategoryExistByTitle(categoryDto.getTitle());

        Category category = ObjectMapperUtils.map(categoryDto, Category.class);
        Category categorySaved = categoryRepository.save(category);

        return ObjectMapperUtils.map(categorySaved, CategoryResponseDto.class);
    }

    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryDto) {
        isCategoryExistById(categoryId);
        categoryDto.setId(categoryId);

        return createCategory(categoryDto);
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        Category categoryExistById = isCategoryExistById(categoryId);
        categoryExistById.setIsLocked(true);
        categoryRepository.save(categoryExistById);

        List<Item> allItemsById = itemRepository.findAllByCategoryAndIsLockedFalse(categoryExistById);
        allItemsById.forEach(item -> {
            item.setIsLocked(false);
            itemRepository.save(item);
        });
    }

    public CategoryResponseDto getCategoryWithItems(Long categoryId) {
        isCategoryExistById(categoryId);
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsLockedFalse(categoryId);

        if (categoryOptional.isEmpty())
            throw new ApplicationException(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
                    ERROR_ITEM_NOT_FOUND.getMessageEN(), ERROR_ITEM_NOT_FOUND.getMessageAR(), LocalDateTime.now()));

        Category category = categoryOptional.get();
        List<Item> itemList = itemRepository.findAllByCategoryAndIsLockedFalse(category);
        category.setItems(itemList);

        return ObjectMapperUtils.map(category, CategoryResponseDto.class);
    }

    public List<CategoryResponseDto> getAllCategories() {
        return ObjectMapperUtils.mapAll(categoryRepository.findAllByIsLockedFalse(), CategoryResponseDto.class);
    }

    private void isCategoryExistByTitle(String title) {
        if (categoryRepository.existsByTitle(title)) {
            throw new ApplicationException(new ErrorResponseDTO(
                    HttpStatus.BAD_REQUEST, ERROR_CATEGORY_TITLE_EXIST.getMessageEN(),
                    LocalDateTime.now()));
        }
    }

    public Category isCategoryExistById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ApplicationException(new ErrorResponseDTO(
                    HttpStatus.BAD_REQUEST, ERROR_ITEM_NOT_FOUND.getMessageEN(),
                    LocalDateTime.now()));
        }
        return categoryRepository.findById(categoryId).get();
    }


}
