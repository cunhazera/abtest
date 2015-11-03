package com.softexpert.resource;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import com.softexpert.persistence.SampleEntity;

@RunWith(Arquillian.class)
public class SampleEntityResourceIT {

	@ArquillianResource
	private URI base;

	private WebTarget target;

	@Deployment(testable = false)
	public static WebArchive deploy() {
		URL webXML = SampleEntityResourceIT.class.getResource("web.xml");
		File[] archives = Maven.resolver().loadPomFromFile("pom.xml")
				.importRuntimeDependencies()
				.resolve()
				.withTransitivity()
				.asFile();
		return ShrinkWrap.create(WebArchive.class)
				.setWebXML(webXML)
				.addAsLibraries(archives);
	}

	@Test
	public void getSamples() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
		List<SampleEntity> response = list("dasdasdadsa");
		MatcherAssert.assertThat(response, Matchers.hasSize(0));
	}

	@Test
	public void post() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
		SampleEntity entity = create("name");
		SampleEntity response = post(entity);
		MatcherAssert.assertThat(response.id, Matchers.notNullValue());
		MatcherAssert.assertThat(response.name, Matchers.equalTo(entity.name));
	}

	@Test
	public void list() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
		SampleEntity entity = create("new name");
		post(entity);
		List<SampleEntity> list = list(entity.name);
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
	}

	private SampleEntity post(SampleEntity entity) {
		return target.path("/v1/samples")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), SampleEntity.class);
	}

	private List<SampleEntity> list(String search) {
		return target.path("/v1/samples").queryParam("search", search)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(List.class);
	}

	private SampleEntity create(String name) {
		SampleEntity entity = new SampleEntity();
		entity.name = name;
		return entity;
	}

}
