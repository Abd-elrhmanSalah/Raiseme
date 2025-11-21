package com.eprogs.raiseme.repository;

import com.eprogs.raiseme.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID extends Number> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}