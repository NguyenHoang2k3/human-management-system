package com.lab.lib.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lab.lib.repository.BaseRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BaseService<T, ID>  {
	protected final BaseRepository<T, ID> repository;
	
	protected BaseService(BaseRepository<T, ID> repository) {
		this.repository = repository;
	}
	
	public T save(T entity) {
        return repository.save(entity);
    }
	
	public T findByFields(Map<String, Object> fields) {
        return repository.findOne(
                        (root, query, criteriaBuilder) -> criteriaBuilder.and(buildPredicates(fields, criteriaBuilder, root).toArray(new Predicate[0])))
                .orElse(null);
    }
	
	private List<Predicate> buildPredicates(Map<String, Object> fields, CriteriaBuilder criteriaBuilder, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();
        fields.forEach((field, value) -> predicates.add(criteriaBuilder.equal(root.get(field), value)));
        return predicates;
    }
}
