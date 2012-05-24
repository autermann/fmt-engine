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
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/user\"", MediaTypes.USER);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/activity\"", MediaTypes.ACTIVITY);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/comment\"", MediaTypes.COMMENT);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/flashmob\"", MediaTypes.FLASHMOB);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/role\"", MediaTypes.ROLE);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/task\"", MediaTypes.TASK);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/user-list\"", MediaTypes.USER_LIST);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/activity-list\"", MediaTypes.ACTIVITY_LIST);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/comment-list\"", MediaTypes.COMMENT_LIST);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/flashmob-list\"", MediaTypes.FLASHMOB_LIST);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/role-list\"", MediaTypes.ROLE_LIST);
		assertEquals("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/task-list\"", MediaTypes.TASK_LIST);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/user-list\""), MediaTypes.USER_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/activity-list\""), MediaTypes.ACTIVITY_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/comment-list\""), MediaTypes.COMMENT_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/flashmob-list\""), MediaTypes.FLASHMOB_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/role-list\""), MediaTypes.ROLE_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/task-list\""), MediaTypes.TASK_LIST_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/user\""), MediaTypes.USER_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/activity\""), MediaTypes.ACTIVITY_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/comment\""), MediaTypes.COMMENT_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/flashmob\""), MediaTypes.FLASHMOB_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/role\""), MediaTypes.ROLE_TYPE);
		assertEquals(MediaType.valueOf("application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/task\""), MediaTypes.TASK_TYPE);

	}

}
