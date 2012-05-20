/*
 * Copyright (C) 2012  Christian Autermann, Dustin Demuth, Maurin Radtke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.ifgi.fmt.web;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.*;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;

import de.ifgi.fmt.utils.constants.RESTConstants.HeaderParams;
import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;

public class FlashMobTest extends JerseyTest {
	protected static final Logger log = LoggerFactory
			.getLogger(FlashMobTest.class);

	@BeforeClass
	public static void initLogger() {
		java.util.logging.Logger rootLogger = java.util.logging.LogManager
				.getLogManager().getLogger("");
		java.util.logging.Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		org.slf4j.bridge.SLF4JBridgeHandler.install();
	}

	@Before
	public void setUp() throws Exception {
		try {
			super.setUp();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Test
	public void testMimeTypes()  {
		assertEquals("application/vnd.flashmobtoolkit.user+json", MediaTypes.USER);
		assertEquals("application/vnd.flashmobtoolkit.activity+json", MediaTypes.ACTIVITY);
		assertEquals("application/vnd.flashmobtoolkit.comment+json", MediaTypes.COMMENT);
		assertEquals("application/vnd.flashmobtoolkit.flashmob+json", MediaTypes.FLASHMOB);
		assertEquals("application/vnd.flashmobtoolkit.role+json", MediaTypes.ROLE);
		assertEquals("application/vnd.flashmobtoolkit.task+json", MediaTypes.TASK);
		
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.user+json"), MediaTypes.USER_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.activity+json"), MediaTypes.ACTIVITY_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.comment+json"), MediaTypes.COMMENT_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.flashmob+json"), MediaTypes.FLASHMOB_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.role+json"), MediaTypes.ROLE_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.task+json"), MediaTypes.TASK_TYPE);
		
		assertEquals("application/vnd.flashmobtoolkit.user.list+json", MediaTypes.USER_LIST);
		assertEquals("application/vnd.flashmobtoolkit.activity.list+json", MediaTypes.ACTIVITY_LIST);
		assertEquals("application/vnd.flashmobtoolkit.comment.list+json", MediaTypes.COMMENT_LIST);
		assertEquals("application/vnd.flashmobtoolkit.flashmob.list+json", MediaTypes.FLASHMOB_LIST);
		assertEquals("application/vnd.flashmobtoolkit.role.list+json", MediaTypes.ROLE_LIST);
		assertEquals("application/vnd.flashmobtoolkit.task.list+json", MediaTypes.TASK_LIST);
		
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.user.list+json"), MediaTypes.USER_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.activity.list+json"), MediaTypes.ACTIVITY_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.comment.list+json"), MediaTypes.COMMENT_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.flashmob.list+json"), MediaTypes.FLASHMOB_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.role.list+json"), MediaTypes.ROLE_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.flashmobtoolkit.task.list+json"), MediaTypes.TASK_LIST_TYPE);
	}

	protected WebResource getWebResource() {
		return this.client().resource(getBaseURI());
	}

	public FlashMobTest() throws Exception {
		super("de.ifgi.fmt");
	}

	@Test
	public void testGetFlashmobs() {
		getWebResource().path(Paths.FLASHMOBS).accept(MediaTypes.FLASHMOB_LIST).get(JSONObject.class);
	}
	
	@Test
	public void testPOSTFlashmobs() {
		getWebResource()
				.path(Paths.FLASHMOBS)
				.accept(MediaTypes.FLASHMOB)
				.header(HeaderParams.CONTENT_TYPE, MediaTypes.FLASHMOB)
				.entity(new JSONObject())
				.post(JSONObject.class);
	}
	

	@Test
	public void testPOSTUsers() {
		getWebResource()
				.path(Paths.USERS)
				.accept(MediaTypes.USER)
				.header(HeaderParams.CONTENT_TYPE, MediaTypes.USER)
				.entity(new JSONObject())
				.post(JSONObject.class);
	}
	@Test
	public void testGetUsers() {
		getWebResource().path(Paths.USERS).accept(MediaTypes.USER_LIST).get(JSONObject.class);
	}

}
