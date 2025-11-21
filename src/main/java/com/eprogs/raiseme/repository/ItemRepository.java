package com.eprogs.raiseme.repository;


import com.eprogs.raiseme.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends BaseRepository<Item, Long> {

}

