package com.softexpert.repository;

import static com.softexpert.persistence.QSampleEntity.sampleEntity;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.softexpert.persistence.SampleEntity;
import com.softexpert.repository.DefaultRepository;

public class DefaultRepositoryTest {

	private static final long DUMMY_ID = 1L;
	@Spy
	@InjectMocks
	private DefaultRepository<SampleEntity> repository;
	@Mock
	private EntityManager entityManager;
	@Mock
	private JPAQuery<SampleEntity> jpaQuery;

	@Before
	public void init() {
		initMocks(this);
		Mockito.when(repository.createQuery()).thenReturn(jpaQuery);
	}

	@Test
	public void findById() {
		Mockito.when(entityManager.find(SampleEntity.class, DUMMY_ID)).thenReturn(createSample());
		SampleEntity sample= repository.findById(1L, SampleEntity.class);
		MatcherAssert.assertThat(sample.id, Matchers.equalTo(DUMMY_ID));
	}

	@Test
	public void save() {
		SampleEntity entity = createSample();
		entity = repository.save(entity);
		Mockito.verify(entityManager).persist(entity);
		Mockito.verify(entityManager, Mockito.never()).merge(entity);
	}

	@Test
	public void edit() {
		SampleEntity entity = createSample();
		repository.edit(entity);
		Mockito.verify(entityManager, Mockito.never()).persist(entity);
		Mockito.verify(entityManager).merge(entity);
	}

	@Test
	public void delete() {
		SampleEntity entity = createSample();
		repository.delete(entity);
		Mockito.verify(entityManager).remove(entity);
	}

	@Test
	public void list() {
		BooleanExpression expression = sampleEntity.id.eq(DUMMY_ID);
		Mockito.when(jpaQuery.select(sampleEntity)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.from(sampleEntity)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.where(expression)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.fetch()).thenReturn(Arrays.asList(createSample(), createSample()));
		List<SampleEntity> list = repository.list(sampleEntity, expression, sampleEntity);
		MatcherAssert.assertThat(list, Matchers.hasSize(2));
		Mockito.verify(jpaQuery).select(sampleEntity);
		Mockito.verify(jpaQuery).from(sampleEntity);
		Mockito.verify(jpaQuery).where(expression);
		Mockito.verify(jpaQuery).fetch();
	}

	@Test
	public void all() {
		Mockito.when(jpaQuery.select(sampleEntity)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.from(sampleEntity)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.fetch()).thenReturn(Arrays.asList(createSample(), createSample()));
		List<SampleEntity> list = repository.all(sampleEntity, sampleEntity);
		MatcherAssert.assertThat(list, Matchers.hasSize(2));
		Mockito.verify(jpaQuery).select(sampleEntity);
		Mockito.verify(jpaQuery).from(sampleEntity);
		Mockito.verify(jpaQuery).fetch();
	}

	private SampleEntity createSample() {
		return new SampleEntity.builder().id(DUMMY_ID).build();
	}
}
