package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LinkTest {

	private Link link;

	@BeforeTest
	public void setUp() {
		link = new Link();
	}

	@Test
	public void testLink() {
		link.setRel("rel");
		link.setUri("uri");

		Assert.assertEquals(link.getRel(), "rel");
		Assert.assertEquals(link.getUri(), "uri");
	}
}
