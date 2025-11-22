package com.eprogs.raiseme.repository;


import com.eprogs.raiseme.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long> {

    Boolean existsByTitle(String title);

    List<Category> findAllByIsLockedFalse();

    Optional<Category> findByIdAndIsLockedFalse(Long categoryId);
}

