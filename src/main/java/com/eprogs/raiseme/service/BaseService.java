package com.eprogs.raiseme.service;


import com.eprogs.raiseme.entity.BaseEntity;
import com.eprogs.raiseme.repository.BaseRepository;
import jakarta.persistence.MappedSuperclass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@MappedSuperclass
@RequiredArgsConstructor
public abstract class BaseService<T extends BaseEntity<ID>, ID extends Number> {

    @Autowired
    private BaseRepository<T, ID> baseRepository;

    public Optional<T> findById(ID id) {

        return baseRepository.findById(id);

    }

    public List<T> findAll() {
        return baseRepository.findAll();
    }

    public List<T> findAll(Specification<T> spec) {
        return baseRepository.findAll(spec);
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return baseRepository.findAll(spec, pageable);
    }

    public T insert(T entity) {
        T saved = baseRepository.save(entity);
        return saved;
    }

    public void insertAll(List<T> entity) {
        baseRepository.saveAll(entity);
    }

    public T delete(ID id) {
        baseRepository.deleteById(id);
        return null;
    }

    public T saveOrUpdate(T entity) {
        T saved = baseRepository.save(entity);
        return saved;
    }

    public T saveOrUpdateAll(List<T> entity) {
        baseRepository.saveAll(entity);
        return null;
    }

    public Long count() {
        return baseRepository.count();
    }
}
