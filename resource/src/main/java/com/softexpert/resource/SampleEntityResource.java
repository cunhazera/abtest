package com.softexpert.resource;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.softexpert.business.SampleEntityService;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.SampleEntity;

@Path("sample")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SampleEntityResource {

	@Inject
	private SampleEntityService service;

	@POST
	public SampleEntity create(@NotNull SampleEntity sample) throws AppException {
		return service.create(sample);
	}

	@PUT
	public SampleEntity edit(@NotNull SampleEntity sample) throws AppException {
		return service.edit(sample);
	}

	@GET
	public List<SampleEntity> list(@QueryParam("search") String search) {
		return service.list(search);
	}

	@GET
	@Path("{id}")
	public SampleEntity find(@NotNull @PathParam("id") Long id) throws AppException {
		return service.find(id);
	}

	@DELETE
	@Path("{id}")
	public void delete(@NotNull @PathParam("id") Long id) throws AppException {
		service.delete(id);
	}

}
