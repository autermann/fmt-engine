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
package de.ifgi.fmt.utils.constants;

import com.google.code.morphia.mapping.Mapper;

public interface ModelConstants {
	public interface Common {
		public static final String ID = Mapper.ID_KEY;
		public static final String LAST_CHANGED = "lastChanged";
		public static final String CREATION_TIME = "creationTime";
	}

	public interface Activity {
		public static final String COLLECTION_NAME = "activities";
		public static final String DESCRIPTION = "description";
		public static final String FLASHMOB = "flashmob";
		public static final String SIGNAL = "signal";
		public static final String TASKS = "savedTasks";
		public static final String TITLE = "title";
		public static final String TRIGGER = "trigger";
	}

	public interface Comment {
		public static final String COLLECTION_NAME = "comments";
		public static final String FLASHMOB = "flashmob";
		public static final String LAST_CHANGED = "lastChanged";
		public static final String TEXT = "text";
		public static final String TIME = "time";
		public static final String USER = "user";

	}

	public interface Flashmob {
		public static final String ACTIVITIES = "activities";
		public static final String COORDINATOR = "coordinator";
		public static final String DESCRIPTION = "description";
		public static final String END = "end";
		public static final String KEY = "key";
		public static final String COLLECTION_NAME = "flashmobs";
		public static final String LOCATION = "location";
		public static final String PUBLIC = "isPublic";
		public static final String PUBLISH = "publish";
		public static final String ROLES = "roles";
		public static final String START = "start";
		public static final String TITLE = "title";
		public static final String TRIGGERS = "triggers";
		public static final String VALIDITY = "validity";

	}

	public interface Role {
		public static final String ACTIVITIES = "activities";
		public static final String CATEGORY = "category";
		public static final String COLLECTION_NAME = "roles";
		public static final String DESCRIPTION = "description";
		public static final String FLASHMOB = "flashmob";
		public static final String ITEMS = "items";
		public static final String MAX_COUNT = "maxCount";
		public static final String MIN_COUNT = "minCount";
		public static final String START_POINT = "startPoint";
		public static final String TITLE = "title";
		public static final String USERS = "users";
	}

	public interface Trigger {
		public static final String COLLECTION_NAME = "triggers";
		public static final String DESCRIPTION = "description";
		public static final String FLASHMOB = "flashmob";
		public static final String LOCATION = "location";
		public static final String TIME = "time";

	}

	public interface User {
		public static final String AUTH_TOKEN = "authToken";
		public static final String COLLECTION_NAME = "users";
		public static final String EMAIL = "email";
		public static final String PASSWORD_HASH = "password";
		public static final String ROLES = "roles";
		public static final String USERNAME = "username";

	}

	public interface Signal {
		public static final String LINK = "link";
		public static final String TEXT = "text";
		public static final String COLLECTION_NAME = "signals";
	}

	public interface Task {
		public static final String ACTIVITY = "activity";
		public static final String COLLECTION_NAME = "tasks";
		public static final String DESCRIPTION = "description";
		public static final String ROLE = "role";
		public static final String LINK = "link";
		public static final String TYPE = "type";
		public static final String LINE = "line";
	}
}
