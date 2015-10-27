package com.softexpert.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class DefaultRepository<T> {

	@PersistenceContext(name = "ExampleDS")
	private EntityManager entityManager;

	public T findById(Long id, Class<T> entityClass) {
		return entityManager.find(entityClass, id);
	}

	public T save(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	public T edit(T entity) {
		return entityManager.merge(entity);
	}

	public void delete(T entity) {
		entityManager.remove(entity);
	}

	public List<T> list(EntityPathBase<T> from, Predicate predicate, Expression<T> select) {
		return createQuery().select(select).from(from).where(predicate).fetch();
	}

	public List<T> all(EntityPathBase<T> entityPathBase, Expression<T> select) {
		return createQuery().select(select).from(entityPathBase).fetch();
	}

	protected JPAQuery<T> createQuery() {
		return new JPAQuery<T>(entityManager, HQLTemplates.DEFAULT);
	}

}