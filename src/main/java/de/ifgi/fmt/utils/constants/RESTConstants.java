package de.ifgi.fmt.utils.constants;

import javax.ws.rs.core.MediaType;


public interface RESTConstants {
	public static abstract class MediaTypes {
		public static final String IMAGE_PNG = "image/png";
		public static final String IMAGE_GIF = "image/gif";
		public static final String IMAGE_JPEG = "image/jpeg";
		
		public static final String MIME_TYPE_PREFIX = "returns application/vnd.flashmobtoolkit.";
		public static final String MIME_TYPE_POSTFIX = "+json";
		
		
		public static final String FLASHMOB = MIME_TYPE_PREFIX + "flashmob" + MIME_TYPE_POSTFIX;
		public static final MediaType FLASHMOB_TYPE = MediaType.valueOf(FLASHMOB);
		public static final String FLASHMOB_LIST = MIME_TYPE_PREFIX + "flashmob.list" + MIME_TYPE_POSTFIX;
		public static final MediaType FLASHMOB_LIST_TYPE = MediaType.valueOf(FLASHMOB);

		public static final String ACTIVITY = MIME_TYPE_PREFIX + "activity" + MIME_TYPE_POSTFIX;
		public static final MediaType ACTIVITY_TYPE = MediaType.valueOf(ACTIVITY);
		public static final String ACTIVITY_LIST = MIME_TYPE_PREFIX + "activity.list" + MIME_TYPE_POSTFIX;
		public static final MediaType ACTIVITY_LIST_TYPE = MediaType.valueOf(ACTIVITY);
		
		public static final String TRIGGER = MIME_TYPE_PREFIX + "trigger" + MIME_TYPE_POSTFIX;
		public static final MediaType TRIGGER_TYPE = MediaType.valueOf(TRIGGER);
		public static final String TRIGGER_LIST = MIME_TYPE_PREFIX + "trigger.list" + MIME_TYPE_POSTFIX;
		public static final MediaType TRIGGER_LIST_TYPE = MediaType.valueOf(TRIGGER);
		
		public static final String ROLE = MIME_TYPE_PREFIX + "role" + MIME_TYPE_POSTFIX;
		public static final MediaType ROLE_TYPE = MediaType.valueOf(ROLE);
		public static final String ROLE_LIST = MIME_TYPE_PREFIX + "role.list" + MIME_TYPE_POSTFIX;
		public static final MediaType ROLE_LIST_TYPE = MediaType.valueOf(ROLE);
		
		public static final String SIGNAL = MIME_TYPE_PREFIX + "signal" + MIME_TYPE_POSTFIX;
		public static final MediaType SIGNAL_TYPE = MediaType.valueOf(SIGNAL);
		public static final String SIGNAL_LIST = MIME_TYPE_PREFIX + "signal.list" + MIME_TYPE_POSTFIX;
		public static final MediaType SIGNAL_LIST_TYPE = MediaType.valueOf(SIGNAL);
		
		public static final String USER = MIME_TYPE_PREFIX + "user" + MIME_TYPE_POSTFIX;
		public static final MediaType USER_TYPE = MediaType.valueOf(USER);
		public static final String USER_LIST = MIME_TYPE_PREFIX + "user.list" + MIME_TYPE_POSTFIX;
		public static final MediaType USER_LIST_TYPE = MediaType.valueOf(USER);

		public static final String TASK = MIME_TYPE_PREFIX + "task" + MIME_TYPE_POSTFIX;
		public static final MediaType TASK_TYPE = MediaType.valueOf(TASK);
		public static final String TASK_LIST = MIME_TYPE_PREFIX + "task.list" + MIME_TYPE_POSTFIX;
		public static final MediaType TASK_LIST_TYPE = MediaType.valueOf(TASK);
		
		public static final String COMMENT = MIME_TYPE_PREFIX + "comment" + MIME_TYPE_POSTFIX;
		public static final MediaType COMMENT_TYPE = MediaType.valueOf(COMMENT);
		public static final String COMMENT_LIST = MIME_TYPE_PREFIX + "comment.list" + MIME_TYPE_POSTFIX;
		public static final MediaType COMMENT_LIST_TYPE = MediaType.valueOf(COMMENT_LIST);
	}

	public static abstract class PathParams {
		public static final String FLASHMOB = "flashmob";
		public static final String FLASHMOB_P = "{" + FLASHMOB + "}";
		public static final String ROLE = "role";
		public static final String ROLE_P = "{" + ROLE + "}";
		public static final String ACTIVITY = "activity";
		public static final String ACTIVITY_P = "{" + ACTIVITY + "}";
		public static final String TRIGGER = "trigger";
		public static final String TRIGGER_P = "{" + TRIGGER + "}";
		public static final String USER = "user";
		public static final String USER_P = "{" + USER + "}";
	}

	public static abstract class QueryParams {
		public static final String LIMIT = "limit";
		public static final String POSITION = "pos";
		public static final String USER = "user";
		public static final String BBOX = "bbox";
		public static final String FROM = "from";
		public static final String TO = "to";
		public static final String SORT = "sort";
		public static final String DESCENDING = "descending";
		public static final String SHOW = "show";
		public static final String SEARCH = "search";
		public static final String PARTICIPANT = "participant";
	}

	public enum Sorting {
		START_TIME, PARTICIPANTS, CREATION_TIME, TITLE;
	}
	
	public enum ShowStatus {
		PUBLIC, PRIVATE;
	}

	public static abstract class HeaderParams {
		public static final String CONTENT_TYPE = "Content-Type";
	}

	public static abstract class Paths {
		public static final String FLASHMOBS = "/flashmobs";
		public static final String FLASHMOB = FLASHMOBS + "/" + PathParams.FLASHMOB_P;
		private static final String ROLES = "/roles";
		private static final String ROLE = ROLES + "/" + PathParams.ROLE_P;
		private static final String ACTIVITIES = "/activities";
		private static final String ACTIVITY = ACTIVITIES + "/" + PathParams.ACTIVITY_P;
		private static final String TRIGGERS = "/triggers";
		private static final String TRIGGER = TRIGGERS + "/" + PathParams.TRIGGER_P;
		public static final String USERS = "/users";
		public static final String USER = USERS + "/" + PathParams.USER_P;
		private static final String TASK = "/task";
		public static final String ROLES_FOR_FLASHMOB = FLASHMOB + ROLES;
		public static final String ROLE_FOR_FLASHMOB = FLASHMOB + ROLE;
		public static final String USERS_OF_ROLE_OF_FLASHMOB = ROLE_FOR_FLASHMOB + USERS;
		public static final String ACTIVITIES_OF_ROLE_OF_FLASHMOB = ROLE_FOR_FLASHMOB + ACTIVITIES;
		public static final String ACTIVITY_OF_ROLE_OF_FLASHMOB = ROLE_FOR_FLASHMOB + ACTIVITY;
		public static final String TASK_OF_ACTIVITY_OF_ROLE_OF_FLASHMOB = ACTIVITY_OF_ROLE_OF_FLASHMOB + TASK;
		public static final String USERS_OF_FLASHMOB = FLASHMOB + USERS;
		public static final String ACTIVITES_OF_FLASHMOB = FLASHMOB + ACTIVITIES;
		public static final String ACTIVITY_OF_FLASHMOB = FLASHMOB + ACTIVITY;
		public static final String ROLES_OF_ACTIVITY_OF_FLASHMOB = ACTIVITY_OF_FLASHMOB + ROLES;
		public static final String ROLE_OF_ACTIVITY_OF_FLASHMOB = ACTIVITY_OF_FLASHMOB + ROLE;
		public static final String TASK_ROLE_OF_ACTIVITY_OF_FLASHMOB = ROLE_OF_ACTIVITY_OF_FLASHMOB + TASK;
		public static final String TRIGGERS_OF_ACTIVITY = ACTIVITES_OF_FLASHMOB + TRIGGERS;
		public static final String SIGNAL_OF_ACTIVITY = ACTIVITES_OF_FLASHMOB + "/signal";
		public static final String TRIGGERS_OF_FLASHMOB = FLASHMOB +TRIGGERS;
		public static final String TRIGGER_OF_FLASHMOB = FLASHMOB +TRIGGER;
		public static final String FLASHMOBS_OF_USER = USER + FLASHMOBS;
		public static final String FLASHMOB_OF_USER = USER + FLASHMOB;
		public static final String ACTIVITIES_OF_FLASHMOB_OF_USER = FLASHMOB_OF_USER + ACTIVITIES;
		public static final String ACTIVITY_OF_FLASHMOB_OF_USER = FLASHMOB_OF_USER + ACTIVITY;
		public static final String TASK_OF_ACTIVITY_OF_FLASHMOB_OF_USER = ACTIVITIES_OF_FLASHMOB_OF_USER + TASK;
		
	}
}
