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

import javax.ws.rs.core.MediaType;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
public interface RESTConstants {
    /**
     * 
     */
    public static abstract class Roles {
	/**
	 * 
	 */
	public static final String GUEST = "GUEST";
		/**
		 * 
		 */
		public static final String USER = "USER";
		/**
		 * 
		 */
		public static final String ADMIN = "ADMIN";
	}
	
    /**
     * 
     */
    public static abstract class MediaTypes {
	    /**
	     * 
	     */
	    public static final String FLASHMOB = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/flashmob\"";
		/**
		 * 
		 */
		public static final String FLASHMOB_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/flashmob-list\"";
		/**
		 * 
		 */
		public static final String ACTIVITY = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/activity\"";
		/**
		 * 
		 */
		public static final String ACTIVITY_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/activity-list\"";
		/**
		 * 
		 */
		public static final String TRIGGER = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/trigger\"";
		/**
		 * 
		 */
		public static final String TRIGGER_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/trigger-list\"";
		/**
		 * 
		 */
		public static final String ROLE = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/role\"";
		/**
		 * 
		 */
		public static final String ROLE_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/role-list\"";
		/**
		 * 
		 */
		public static final String SIGNAL = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/signal\"";
		/**
		 * 
		 */
		public static final String SIGNAL_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/signal-list\"";
		/**
		 * 
		 */
		public static final String USER = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/user\"";
		/**
		 * 
		 */
		public static final String USER_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/user-list\"";
		/**
		 * 
		 */
		public static final String TASK = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/task\"";
		/**
		 * 
		 */
		public static final String TASK_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/task-list\"";
		/**
		 * 
		 */
		public static final String COMMENT = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/comment\"";
		/**
		 * 
		 */
		public static final String COMMENT_LIST = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/comment-list\"";
		
		/**
		 * 
		 */
		public static final MediaType FLASHMOB_TYPE = MediaType.valueOf(FLASHMOB);
		/**
		 * 
		 */
		public static final MediaType FLASHMOB_LIST_TYPE = MediaType.valueOf(FLASHMOB_LIST);
		/**
		 * 
		 */
		public static final MediaType ACTIVITY_TYPE = MediaType.valueOf(ACTIVITY);
		/**
		 * 
		 */
		public static final MediaType ACTIVITY_LIST_TYPE = MediaType.valueOf(ACTIVITY_LIST);
		/**
		 * 
		 */
		public static final MediaType TRIGGER_TYPE = MediaType.valueOf(TRIGGER);
		/**
		 * 
		 */
		public static final MediaType TRIGGER_LIST_TYPE = MediaType.valueOf(TRIGGER_LIST);
		/**
		 * 
		 */
		public static final MediaType ROLE_TYPE = MediaType.valueOf(ROLE);
		/**
		 * 
		 */
		public static final MediaType ROLE_LIST_TYPE = MediaType.valueOf(ROLE_LIST);
		/**
		 * 
		 */
		public static final MediaType SIGNAL_TYPE = MediaType.valueOf(SIGNAL);
		/**
		 * 
		 */
		public static final MediaType SIGNAL_LIST_TYPE = MediaType.valueOf(SIGNAL_LIST);
		/**
		 * 
		 */
		public static final MediaType USER_TYPE = MediaType.valueOf(USER);
		/**
		 * 
		 */
		public static final MediaType USER_LIST_TYPE = MediaType.valueOf(USER_LIST);
		/**
		 * 
		 */
		public static final MediaType TASK_TYPE = MediaType.valueOf(TASK);
		/**
		 * 
		 */
		public static final MediaType TASK_LIST_TYPE = MediaType.valueOf(TASK_LIST);
		/**
		 * 
		 */
		public static final MediaType COMMENT_TYPE = MediaType.valueOf(COMMENT);
		/**
		 * 
		 */
		public static final MediaType COMMENT_LIST_TYPE = MediaType.valueOf(COMMENT_LIST);
		
		/**
		 * 
		 */
		public static final String ERRORS = "application/json; profile=\"http://giv-flashmob.uni-muenster.de/schema/errors\"";
		/**
		 * 
		 */
		public static final MediaType ERRORS_TYPE = MediaType.valueOf(ERRORS);
	}

	/**
	 * 
	 */
	public static abstract class PathParams {
	    /**
	     * 
	     */
	    public static final String FLASHMOB = "flashmob";
		/**
		 * 
		 */
		public static final String ROLE = "role";
		/**
		 * 
		 */
		public static final String ACTIVITY = "activity";
		/**
		 * 
		 */
		public static final String TRIGGER = "trigger";
		/**
		 * 
		 */
		public static final String USER = "user";
		/**
		 * 
		 */
		public static final String COMMENT = "comment";
		private static final String COMMENT_P = "{" + COMMENT + "}";
		private static final String FLASHMOB_P = "{" + FLASHMOB + "}";
		private static final String ROLE_P = "{" + ROLE + "}";
		private static final String ACTIVITY_P = "{" + ACTIVITY + "}";
		private static final String TRIGGER_P = "{" + TRIGGER + "}";
		private static final String USER_P = "{" + USER + "}";
	}

	/**
	 * 
	 */
	public static abstract class QueryParams {
	    /**
	     * 
	     */
	    public static final String LIMIT = "limit";
		/**
		 * 
		 */
		public static final String POSITION = "pos";
		/**
		 * 
		 */
		public static final String USER = "user";
		/**
		 * 
		 */
		public static final String BBOX = "bbox";
		/**
		 * 
		 */
		public static final String FROM = "from";
		/**
		 * 
		 */
		public static final String TO = "to";
		/**
		 * 
		 */
		public static final String SORT = "sort";
		/**
		 * 
		 */
		public static final String DESCENDING = "descending";
		/**
		 * 
		 */
		public static final String SHOW = "show";
		/**
		 * 
		 */
		public static final String SEARCH = "search";
		/**
		 * 
		 */
		public static final String PARTICIPANT = "participant";
		/**
		 * 
		 */
		public static final String MIN_PARTICIPANTS = "minParticipants";
		/**
		 * 
		 */
		public static final String MAX_PARTICIPANTS = "maxParticipants";
	}

	/**
	 * 
	 */
	public enum Sorting {
	    /**
	     * 
	     */
	    START_TIME, 
		/**
		 * 
		 */
		PARTICIPANTS, 
		/**
		 * 
		 */
		CREATION_TIME,
		/**
		 * 
		 */
		DISTANCE,
		/**
		 * 
		 */
		TITLE;
		
		//TODO private String order;
	}

	/**
	 * 
	 */
	public enum ShowStatus {

	    /**
	     * 
	     */
	    PUBLIC,
	    /**
	     * 
	     */
	    PRIVATE;
	}

	/*
	 * TODO refactor URL's to enable access to resource params.
	 * e.g 
	 * 	ROLES="/roles"
	 *  ROLE ="/{role}"
	 *	servlet subresources 
	 *	@Path(ROLES)
	 *	class RolesServlet {
	 *		@Path(ROLE)
	 *		public Role getRole(@PathParam(PathParams.ROLE) ObjectId role) {
	 *			return bla;
	 *		}
	 *	}
	 * 
	 */
	/**
	 * 
	 */
	public static abstract class Paths {
	    public static final String ROOT = "/"; 
		public static final String FLASHMOBS = "/flashmobs";
		public static final String FLASHMOB = FLASHMOBS + "/" + PathParams.FLASHMOB_P;
		public static final String ROLES = "/roles";
		public static final String COMMENTS = "/comments";
		public static final String COMMENT = COMMENTS + "/" + PathParams.COMMENT_P;
		public static final String ROLE = ROLES + "/" + PathParams.ROLE_P;
		public static final String ACTIVITIES = "/activities";
		public static final String ACTIVITY = ACTIVITIES + "/" + PathParams.ACTIVITY_P;
		public static final String TRIGGERS = "/triggers";
		public static final String TRIGGER = TRIGGERS + "/" + PathParams.TRIGGER_P;
		public static final String USERS = "/users";
		public static final String USER = USERS + "/" + PathParams.USER_P;
		public static final String TASK = "/task";
		public static final String ROLES_FOR_FLASHMOB = FLASHMOB + ROLES;
		public static final String ROLE_FOR_FLASHMOB = FLASHMOB + ROLE;
		public static final String COMMENTS_FOR_FLASHMOB = FLASHMOB + COMMENTS;
		public static final String COMMENT_FOR_FLASHMOB = FLASHMOB + COMMENT;
		public static final String USERS_OF_ROLE_OF_FLASHMOB = ROLE_FOR_FLASHMOB + USERS;
		public static final String USER_OF_ROLE_OF_FLASHMOB = ROLE_FOR_FLASHMOB + USER;
		public static final String ACTIVITIES_OF_ROLE_OF_FLASHMOB = ROLE_FOR_FLASHMOB + ACTIVITIES;
		public static final String ACTIVITY_OF_ROLE_OF_FLASHMOB = ROLE_FOR_FLASHMOB + ACTIVITY;
		public static final String TASK_OF_ACTIVITY_OF_ROLE_OF_FLASHMOB = ACTIVITY_OF_ROLE_OF_FLASHMOB + TASK;
		public static final String USERS_OF_FLASHMOB = FLASHMOB + USERS;
		public static final String ACTIVITIES_OF_FLASHMOB = FLASHMOB + ACTIVITIES;
		public static final String ACTIVITY_OF_FLASHMOB = FLASHMOB + ACTIVITY;
		public static final String ROLES_OF_ACTIVITY_OF_FLASHMOB = ACTIVITY_OF_FLASHMOB + ROLES;
		public static final String ROLE_OF_ACTIVITY_OF_FLASHMOB = ACTIVITY_OF_FLASHMOB + ROLE;
		public static final String TASK_OF_ROLE_OF_ACTIVITY_OF_FLASHMOB = ROLE_OF_ACTIVITY_OF_FLASHMOB + TASK;
		public static final String TRIGGER_OF_ACTIVITY = ACTIVITY_OF_FLASHMOB + "/trigger";
		public static final String SIGNAL = "/signal";
		public static final String SIGNAL_OF_ACTIVITY = ACTIVITY_OF_FLASHMOB + SIGNAL;
		public static final String TRIGGERS_OF_FLASHMOB = FLASHMOB + TRIGGERS;
		public static final String TRIGGER_OF_FLASHMOB = FLASHMOB + TRIGGER;
		public static final String FLASHMOBS_OF_USER = USER + FLASHMOBS;
		public static final String FLASHMOB_OF_USER = USER + FLASHMOB;
		public static final String ROLE_OF_USER_IN_FLASHMOB = FLASHMOB_OF_USER + "/role";
		public static final String ACTIVITIES_OF_FLASHMOB_OF_USER = FLASHMOB_OF_USER + ACTIVITIES;
		public static final String ACTIVITY_OF_FLASHMOB_OF_USER = FLASHMOB_OF_USER + ACTIVITY;
		public static final String TASK_OF_ACTIVITY_OF_FLASHMOB_OF_USER = ACTIVITY_OF_FLASHMOB_OF_USER + TASK;
	}
}
