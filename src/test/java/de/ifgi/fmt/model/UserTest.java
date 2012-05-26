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
package de.ifgi.fmt.model;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class UserTest {

	protected User u;

	@Before
	public void before() {
		u = new User();
	}

	@Test
	public void testPassword() {
		testValidPassword("au89audsjf23#");
		testInvalidPassword("abcde:fg");
		testInvalidPassword("abcde");
	}
	
	/* is tested while persisting */
	@Ignore@Test
	public void testUsername() {
		testValidUsername("auti");
		testValidUsername("autermann");
		testValidUsername("c_autermann");
		testValidUsername("c_aute01");
		testInvalidUsername("ab");
		testInvalidUsername("Christian Autermann");
	}
	
	/* is tested while persisting */
	@Ignore@Test
	public void testEmail() {
		testValidEmail("autermann@uni-muenster.de");
		testValidEmail("autermann@math.uni-muenster.de");
		testValidEmail("autermann+fmt@math.uni-muenster.de");
		testValidEmail("c_autermann+fmt@math.uni-muenster.de");
		testValidEmail("c_autermann+fmt@math.uni-muenster.com");
		testValidEmail("c_autermann+fmt@math.uni-muenster.info");
		testValidEmail("c_autermann+fmt@inf.geo.uni-muenster.info");
		testInvalidEmail("autermann @uni-muenster.de");
		testInvalidEmail("autermann.uni-muenster.de");
		testInvalidEmail("auterman&n@uni-muenster.de");
		testInvalidEmail("autermann@uni-muenster");
		testInvalidEmail("autermann@uni muenster.de");
	}

	private void testInvalidEmail(String s) {
		boolean exep = false;
		try { u.setEmail(s); } catch (Throwable t) { exep = true; }
		if (!exep) fail("'" + s + "' should be invalid");
	}

	private void testValidEmail(String mail) {
		try { u.setEmail(mail); } 
		catch (Throwable t) { fail("'" + mail + "' should be a valid email address"); }
	}
	
	private void testInvalidPassword(String s) {
		boolean exep = false;
		try { u.setPassword(s); } catch (Throwable t) { exep = true; }
		if (!exep) fail("'" + s + "' should be invalid");
	}

	private void testValidPassword(String s) {
		try { u.setPassword(s); } 
		catch (Throwable t) { fail("'" + s + "' should be a valid password"); }
	}
	
	private void testInvalidUsername(String s) {
		boolean exep = false;
		try { u.setUsername(s); } catch (Throwable t) { exep = true; }
		if (!exep) fail("'" + s + "' should be invalid");
	}

	private void testValidUsername(String s) {
		try { u.setUsername(s); } 
		catch (Throwable t) { fail("'" + s + "' should be a valid username"); }
	}

}
