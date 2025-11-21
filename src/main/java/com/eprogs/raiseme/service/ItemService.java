package com.eprogs.raiseme.service;

import com.eprogs.raiseme.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService extends BaseService<Item, Long> {
}
