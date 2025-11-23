package com.eprogs.raiseme.repository;


import com.eprogs.raiseme.entity.Category;
import com.eprogs.raiseme.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends BaseRepository<Item, Long> {

    List<Item> findAllByCategoryAndIsLockedFalse(Category category);

    List<Item> findAllByIsLockedFalse();

    List<Item> findAllByIsBooked(Boolean isBooked);

    List<Item> findAllByIsLockedFalseAndPriceBetween(Double min, Double max);
}

