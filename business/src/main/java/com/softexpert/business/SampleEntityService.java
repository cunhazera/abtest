package com.softexpert.business;


import javax.ejb.Stateless;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.softexpert.persistence.QSampleEntity;
import com.softexpert.persistence.SampleEntity;

@Stateless
public class SampleEntityService extends AbstractService<SampleEntity> {

	@Override
	protected EntityPathBase<SampleEntity> getEntityPathBase() {
		return QSampleEntity.sampleEntity;
	}

	@Override
	protected Class<SampleEntity> getEntityClass() {
		return SampleEntity.class;
	}

	@Override
	protected Expression<SampleEntity> getSelect() {
		return  QSampleEntity.sampleEntity;
	}

	@Override
	protected Predicate getFilter(String schearch) {
		return  QSampleEntity.sampleEntity.name.containsIgnoreCase(schearch);
	}

}
