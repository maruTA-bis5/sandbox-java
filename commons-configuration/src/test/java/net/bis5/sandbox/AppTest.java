package net.bis5.sandbox;

import  org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.ex.*;

public class AppTest {
	@Test
	public void testLoadIni() throws ConfigurationException {
		String fileName = "config.ini";

		Configuration conf = new App().loadIni(fileName);
		conf.getKeys().forEachRemaining(System.err::println);

		assertTrue(conf.containsKey(ConfigKey.BACKEND_URL.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_USER.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_PASSWORD.get()));
	}

	@Test
	public void testLoadProperties() throws ConfigurationException {
		String fileName = "config.properties";

		Configuration conf = new App().loadProperties(fileName);
		conf.getKeys().forEachRemaining(System.err::println);

		assertTrue(conf.containsKey(ConfigKey.BACKEND_URL.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_USER.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_PASSWORD.get()));
	}
}
