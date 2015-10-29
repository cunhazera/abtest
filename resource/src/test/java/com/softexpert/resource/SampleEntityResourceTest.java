package com.softexpert.resource;

import java.io.File;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.SampleEntity;

@RunWith(Arquillian.class)
public class SampleEntityResourceTest {

	@ArquillianResource
	private URI base;

	private static WebTarget target;

	@Deployment(testable = false)
	public static WebArchive deploy() {
		File webXML = new File("src/main/webapp/WEB-INF/web.xml");
		return ShrinkWrap.create(WebArchive.class)
				.addClasses(SampleEntityResource.class, SampleEntity.class, AppException.class).setWebXML(webXML);
	}

	@Test
	public void getSamples() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
		Response response = target.path("/v1/samples").request().get();
		MatcherAssert.assertThat(response.getStatus(), Matchers.equalTo(200));
	}
}
