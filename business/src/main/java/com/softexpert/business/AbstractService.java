package com.softexpert.business;

import java.util.List;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.common.base.Strings;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.softexpert.business.exception.AppException;
import com.softexpert.repository.DefaultRepository;

public abstract class AbstractService<T> {

	@Inject
	protected DefaultRepository<T> repository;
	@Inject
	private Validator validator;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public T find(Long id) throws AppException {
		try {
			return repository.findById(id, getEntityClass());
		} catch (Exception e) {
			throw new AppException("Ops... ocorreu erro ao buscas, registro pode não existir mais.");
		}
	}

	public T create(T entity) throws AppException {
		validation(entity);
		try {
			return repository.save(entity);
		} catch (Exception e) {
			throw new AppException(
					"Ops... erro ao salvar, verifique se todos os dados então preenchidos corretamente :(");
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> list(String schearch) {
		if (Strings.isNullOrEmpty(schearch))
			return repository.all(getEntityPathBase(), getSelect());
		return repository.list(getEntityPathBase(), getFilter(schearch), getSelect());
	}

	public T edit(T entity) throws AppException {
		validation(entity);
		try {
			return repository.edit(entity);
		} catch (Exception e) {
			throw new AppException(
					"Ops... registro não pode ser editado, verifique se todos os dados então preenchidos corretamente.");
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(Long id) throws AppException {
		try {
			repository.delete(find(id));
		} catch (Exception e) {
			throw new AppException("Ops... registro não pode ser removido ou está send utilizado.");
		}
	}

	protected abstract EntityPathBase<T> getEntityPathBase();

	protected abstract Class<T> getEntityClass();

	protected abstract Expression<T> getSelect();

	protected abstract Predicate getFilter(String schearch);

	private void validation(T entity) throws AppException {
		Set<ConstraintViolation<T>> violations = validator.validate(entity);
		if (violations.size() > 0)
			throw new AppException(violations.iterator().next().getMessage());
	}
}
