package com.softexpert.business;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.querydsl.core.types.Predicate;
import com.softexpert.business.AbstractService;
import com.softexpert.business.SampleEntityService;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.QSampleEntity;
import com.softexpert.persistence.SampleEntity;
import com.softexpert.repository.DefaultRepository;

public class SampleEntityServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private SampleEntityService service;
	@Mock
	private DefaultRepository<SampleEntity> repository;
	@Mock
	private Validator validator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected=AppException.class)
	public void createWithValidationError() throws AppException {
		SampleEntity sample = createSampleEntity(ID, "IPI");
		Set<ConstraintViolation<SampleEntity>> violations =  new HashSet<>();
		ConstraintViolation constraintViolation = Mockito.mock(ConstraintViolation.class);
		violations.add(constraintViolation);
		Mockito.when(constraintViolation.getMessage()).thenReturn("Error");
		Mockito.when(validator.validate(sample)).thenReturn(violations);

		service.create(sample);
	}

	@Test
	public void create() throws AppException {
		SampleEntity sample = createSampleEntity(ID, "IPI");
		Mockito.when(repository.save(sample)).thenReturn(createSampleEntity(ID, "IPI"));

		SampleEntity newSample = service.create(sample);

		Mockito.verify(repository).save(sample);
		MatcherAssert.assertThat(newSample.id, Matchers.equalTo(ID));
	}
	
	@Test
	public void edit() throws AppException {
		SampleEntity sample = createSampleEntity(ID, "IPI");
		Mockito.when(repository.edit(sample)).thenReturn(createSampleEntity(ID, null));

		SampleEntity newSample = service.edit(sample);

		Mockito.verify(repository).edit(sample);
		Mockito.verify(validator).validate(sample);
		MatcherAssert.assertThat(newSample.id, Matchers.equalTo(ID));
	}

	@Test
	public void delete() throws AppException {
		Mockito.when(repository.findById(ID, SampleEntity.class)).thenReturn(createSampleEntity(ID, null));

		service.delete(ID);

		Mockito.verify(repository).delete(createSampleEntity(ID, null));
	}
	
	@Test(expected=AppException.class)
	public void deleteWithError() throws AppException {
		Mockito.when(repository.findById(ID, SampleEntity.class)).thenThrow(new IllegalArgumentException("Error"));

		service.delete(ID);

		Mockito.verify(repository).delete(createSampleEntity(ID, null));
	}

	@Test
	public void listAll() {
		Mockito.when(repository.all(QSampleEntity.sampleEntity, QSampleEntity.sampleEntity)).thenReturn(getList());
		List<SampleEntity> list = service.list("");

		Mockito.verify(repository).all(QSampleEntity.sampleEntity, QSampleEntity.sampleEntity);

		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(createSampleEntity(ID, null)));
	}

	@Test
	public void find() throws AppException {
		Mockito.when(repository.findById(ID, SampleEntity.class)).thenReturn(createSampleEntity(ID, null));

		SampleEntity sample = service.find(ID);

		Mockito.verify(repository).findById(ID, SampleEntity.class);
		MatcherAssert.assertThat(sample, Matchers.equalTo(createSampleEntity(ID, null)));
	}

	@Test
	public void getFilter() throws AppException {
		String schearch = "Lala";
		Predicate filter = service.getFilter(schearch);
		MatcherAssert.assertThat(filter,
				Matchers.equalTo(QSampleEntity.sampleEntity.name.containsIgnoreCase(schearch)));
	}

	@Test
	public void validateExntedsClass() {
		assertThat(service, Matchers.instanceOf(AbstractService.class));
	}

	@Test(expected = AppException.class)
	public void findByIdWithError() throws AppException {
		when(repository.findById(ID, SampleEntity.class)).thenThrow(new IllegalArgumentException("Error"));
		service.find(ID);
	}

	@Test(expected = AppException.class)
	public void editWithError() throws AppException {
		when(repository.edit(createSampleEntity(ID, null))).thenThrow(new IllegalArgumentException("Error"));
		service.edit(createSampleEntity(ID, null));
	}

	@Test(expected = AppException.class)
	public void createWithError() throws AppException {
		when(repository.save(createSampleEntity(ID, null))).thenThrow(new IllegalArgumentException("Error"));
		service.create(createSampleEntity(ID, null));
	}

	@Test
	public void listWithError() {
		String schearch = "search";
		Mockito.when(repository.list(QSampleEntity.sampleEntity, service.getFilter(schearch), QSampleEntity.sampleEntity)).thenReturn(getList());
		List<SampleEntity> list = service.list(schearch);

		Mockito.verify(repository).list(QSampleEntity.sampleEntity, service.getFilter(schearch), QSampleEntity.sampleEntity);

		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(createSampleEntity(ID, null)));
	}

	private List<SampleEntity> getList() {
		return Collections.singletonList(createSampleEntity(ID, null));
	}

	private SampleEntity createSampleEntity(Long id, String name) {
		SampleEntity sample = new SampleEntity();
		sample.id = id;
		sample.name = name;
		return sample;
	}
	
}
