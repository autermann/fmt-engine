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
package de.ifgi.fmt.json.impl;

import static de.ifgi.fmt.utils.constants.JSONConstants.ACTIVITIES_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.COMMENTS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.COORDINATOR_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.DESCRIPTION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.END_TIME_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.HREF_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ID_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.KEY_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.LOCATION_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.PARTICIPANTS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.PUBLIC_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.PUBLISH_TIME_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.REQUIRED_USERS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.ROLES_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.START_TIME_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TITLE_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.TRIGGERS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.USERS_KEY;
import static de.ifgi.fmt.utils.constants.JSONConstants.VALIDITY_KEY;

import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.json.JSONFactory.Decodes;
import de.ifgi.fmt.json.JSONFactory.Encodes;
import de.ifgi.fmt.json.JSONHandler;
import de.ifgi.fmt.json.JSONHandler.DefaultView;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.utils.constants.RESTConstants.PathParams;
import de.ifgi.fmt.utils.constants.RESTConstants.Paths;
import de.ifgi.fmt.utils.constants.RESTConstants.View;

/**
 * 
 * @author Autermann, Demuth, Radtke
 */
@Decodes(Flashmob.class)
@Encodes(Flashmob.class)
@DefaultView(View.FLASHMOB)
public class FlashmobHandler extends JSONHandler<Flashmob> {
	private static final Logger log = LoggerFactory
			.getLogger(FlashmobHandler.class);

	@Override
	public Flashmob decode(JSONObject j) throws JSONException {
		log.debug("Decoding Flashmob {}", j);
		Flashmob f = new Flashmob();

		f.setId(parseId(j));
		f.setTitle(j.optString(TITLE_KEY, null));
		f.setDescription(j.optString(DESCRIPTION_KEY, null));
		f.setKey(j.optString(KEY_KEY, null));
		f.setLocation(parseGeometry(j, Point.class, LOCATION_KEY));
		f.setStart(parseTime(j, START_TIME_KEY));
		f.setEnd(parseTime(j, END_TIME_KEY));
		f.setPublish(parseTime(j, PUBLISH_TIME_KEY));
		f.setPublic(parseBoolean(j, PUBLIC_KEY));

		String coordinator = j.optString(COORDINATOR_KEY, null);
		if (coordinator != null) {
			f.setCoordinator(new User().setUsername(coordinator));
		}
		return f;
	}

	@Override
	protected void encodeObject(JSONObject j, Flashmob f, UriInfo uri)
			throws JSONException {
		j.put(ID_KEY, f.getId());
		j.put(TITLE_KEY, f.getTitle());
		switch (f.getView()) {
		case FLASHMOB:
		case FLASHMOB_OF_USER:
			j.put(VALIDITY_KEY, f.getValidity());
			j.put(COORDINATOR_KEY, encode(f, f.getCoordinator(), uri));
		case FLASHMOBS:
		case FLASHMOBS_OF_USER:
			j.put(LOCATION_KEY, encodeGeometry(f.getLocation()));
			j.put(DESCRIPTION_KEY, f.getDescription());
			j.put(START_TIME_KEY, encodeTime(f.getStart()));
			j.put(END_TIME_KEY, encodeTime(f.getEnd()));
			j.put(PUBLISH_TIME_KEY, encodeTime(f.getPublish()));
			j.put(KEY_KEY, f.getKey());
			j.put(REQUIRED_USERS_KEY, f.getRequiredUsers());
			j.put(USERS_KEY, f.getRegisteredUsers());
			j.put(PUBLIC_KEY, f.isPublic());

		}
		if (uri != null) {
			/* set href attribute */
			switch (f.getView()) {
			case FLASHMOB:
			case FLASHMOB_OF_USER:
				/* do nothing */
				break;
			case USER:
			case USERS:
			case FLASHMOBS_OF_USER:
			case ROLE_OF_USER_IN_FLASHMOB:
			case ACTIVITIES_OF_FLASHMOB_OF_USER:
			case ACTIVITY_OF_FLASHMOB_OF_USER:
			case TASK_OF_ACTIVITY_OF_FLASHMOB_OF_USER:
			case SIGNAL_OF_ACTIVITY_OF_FLASHMOB_OF_USER:
				/* user based flashmob */
				j.put(HREF_KEY,
						uri.getBaseUriBuilder()
								.path(Paths.FLASHMOB_OF_USER)
								.build(uri.getPathParameters().getFirst(
										PathParams.USER), f.getId()));
				break;
			default:
				j.put(HREF_KEY, uri.getBaseUriBuilder().path(Paths.FLASHMOB)
						.build(f.getId()));
			}
		}

	}

	@Override
	protected void encodeUris(JSONObject j, Flashmob f, UriInfo uri)
			throws JSONException {

		switch (f.getView()) {
		case FLASHMOB:
			j.put(ACTIVITIES_KEY,
					uri.getBaseUriBuilder().path(Paths.ACTIVITIES_OF_FLASHMOB)
							.build(f));
			j.put(ROLES_KEY,
					uri.getBaseUriBuilder().path(Paths.ROLES_FOR_FLASHMOB)
							.build(f));
			j.put(TRIGGERS_KEY,
					uri.getBaseUriBuilder().path(Paths.TRIGGERS_OF_FLASHMOB)
							.build(f));
			j.put(COMMENTS_KEY,
					uri.getBaseUriBuilder().path(Paths.COMMENTS_FOR_FLASHMOB)
							.build(f));
			j.put(PARTICIPANTS_KEY,
					uri.getBaseUriBuilder().path(Paths.USERS_OF_FLASHMOB)
							.build(f));
			break;
		case FLASHMOBS_OF_USER:
			// TODO:
			j.put(ACTIVITIES_KEY,
					uri.getBaseUriBuilder().path(Paths.ACTIVITIES_OF_FLASHMOB)
							.build(f));
			j.put(ROLES_KEY,
					uri.getBaseUriBuilder().path(Paths.ROLES_FOR_FLASHMOB)
							.build(f));
			j.put(TRIGGERS_KEY,
					uri.getBaseUriBuilder().path(Paths.TRIGGERS_OF_FLASHMOB)
							.build(f));
			j.put(COMMENTS_KEY,
					uri.getBaseUriBuilder().path(Paths.COMMENTS_FOR_FLASHMOB)
							.build(f));
			j.put(PARTICIPANTS_KEY,
					uri.getBaseUriBuilder().path(Paths.USERS_OF_FLASHMOB)
							.build(f));
			break;
		}

	}

}
