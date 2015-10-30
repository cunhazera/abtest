package com.softexpert.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.softexpert.business.SampleEntityService;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.SampleEntity;

@Path("samples")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class SampleEntityResource {
	
	@Inject
	private SampleEntityService service;

	@GET
	public List<SampleEntity> get(@QueryParam("search") String search) {
		return service.list(search);
	}
	
	@POST
	public SampleEntity save(SampleEntity entity) throws AppException{
		return service.create(entity);
	}


}
