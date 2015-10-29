package com.softexpert.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.softexpert.persistence.SampleEntity;

@Path("samples")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class SampleEntityResource {

	@GET
	public SampleEntity get() {
		SampleEntity sampleEntity = new SampleEntity();
		sampleEntity.id = 1L;
		sampleEntity.name = "Test";
		return sampleEntity;
	}


}
