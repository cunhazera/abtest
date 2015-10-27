package com.softexpert.business.exception;

import javax.ws.rs.core.Response;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class AppExceptionMapperTest {

	@Test
	public void create(){
		AppExceptionMapper appExceptionMapper = new AppExceptionMapper();
		String message = "Test";
		Response response = appExceptionMapper.toResponse(new AppException(message));
		MatcherAssert.assertThat(response.getEntity(), Matchers.equalTo(message));
		MatcherAssert.assertThat(response.getStatus(), Matchers.equalTo(404));
	}
}
