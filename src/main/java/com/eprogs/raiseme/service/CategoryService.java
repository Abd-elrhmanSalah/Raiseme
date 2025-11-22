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
    public void deleteCategory(Long categoryId) {
        Category categoryExistById = isCategoryExistById(categoryId);
        categoryExistById.setIsLocked(true);
        categoryRepository.save(categoryExistById);

        List<Item> allItemsById = itemRepository.findAllByCategory(categoryExistById);
        allItemsById.forEach(item -> {
            item.setIsLocked(false);
            itemRepository.save(item);
        });
    }

    public CategoryResponseDto getCategoryWithItems(Long categoryId) {
        isCategoryExistById(categoryId);
        Optional<Category> category = categoryRepository.findByIdAndIsLockedFalse(categoryId);

        if (category.isEmpty())
            throw new ApplicationException(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
                    ERROR_ITEM_NOT_FOUND.getMessageEN(), ERROR_ITEM_NOT_FOUND.getMessageAR(), LocalDateTime.now()));

        return ObjectMapperUtils.map(category.get(),
                CategoryResponseDto.class);
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

    private Category isCategoryExistById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ApplicationException(new ErrorResponseDTO(
                    HttpStatus.BAD_REQUEST, ERROR_ITEM_NOT_FOUND.getMessageEN(),
                    LocalDateTime.now()));
        }
        return categoryRepository.findById(categoryId).get();
    }


}
