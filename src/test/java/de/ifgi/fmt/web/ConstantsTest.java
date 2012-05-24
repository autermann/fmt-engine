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

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import de.ifgi.fmt.utils.constants.RESTConstants.MediaTypes;

public class ConstantsTest extends AbstractFlashMobTest {
	
	@Test
	public void testMimeTypes()  {
		assertEquals("application/vnd.fmt.user+json", MediaTypes.USER);
		assertEquals("application/vnd.fmt.activity+json", MediaTypes.ACTIVITY);
		assertEquals("application/vnd.fmt.comment+json", MediaTypes.COMMENT);
		assertEquals("application/vnd.fmt.flashmob+json", MediaTypes.FLASHMOB);
		assertEquals("application/vnd.fmt.role+json", MediaTypes.ROLE);
		assertEquals("application/vnd.fmt.task+json", MediaTypes.TASK);
		assertEquals("application/vnd.fmt.user-list+json", MediaTypes.USER_LIST);
		assertEquals("application/vnd.fmt.activity-list+json", MediaTypes.ACTIVITY_LIST);
		assertEquals("application/vnd.fmt.comment-list+json", MediaTypes.COMMENT_LIST);
		assertEquals("application/vnd.fmt.flashmob-list+json", MediaTypes.FLASHMOB_LIST);
		assertEquals("application/vnd.fmt.role-list+json", MediaTypes.ROLE_LIST);
		assertEquals("application/vnd.fmt.task-list+json", MediaTypes.TASK_LIST);
		assertEquals(MediaType.valueOf("application/vnd.fmt.user-list+json"), MediaTypes.USER_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.activity-list+json"), MediaTypes.ACTIVITY_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.comment-list+json"), MediaTypes.COMMENT_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.flashmob-list+json"), MediaTypes.FLASHMOB_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.role-list+json"), MediaTypes.ROLE_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.task-list+json"), MediaTypes.TASK_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.user+json"), MediaTypes.USER_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.activity+json"), MediaTypes.ACTIVITY_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.comment+json"), MediaTypes.COMMENT_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.flashmob+json"), MediaTypes.FLASHMOB_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.role+json"), MediaTypes.ROLE_TYPE);
		assertEquals(MediaType.valueOf("application/vnd.fmt.task+json"), MediaTypes.TASK_TYPE);

	}

}
