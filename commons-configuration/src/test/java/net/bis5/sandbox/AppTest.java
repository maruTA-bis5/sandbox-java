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

		assertTrue(conf.containsKey(ConfigKey.BACKEND_URL.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_USER.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_PASSWORD.get()));
	}

	@Test
	public void testLoadProperties() throws ConfigurationException {
		String fileName = "config.properties";

		Configuration conf = new App().loadProperties(fileName);

		assertTrue(conf.containsKey(ConfigKey.BACKEND_URL.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_USER.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_PASSWORD.get()));
	}

	@Test
	public void testLoadYaml() throws ConfigurationException {
		String fileName = "config.yaml";

		Configuration conf = new App().loadYaml(fileName);

		assertTrue(conf.containsKey(ConfigKey.BACKEND_URL.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_USER.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_PASSWORD.get()));
	}

	@Test
	public void testLoadSystemProperties() throws ConfigurationException {
		Configuration conf = new App().loadSystemProperties();

		assertTrue(conf.containsKey(ConfigKey.BACKEND_URL.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_USER.get()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_PASSWORD.get()));
		assertEquals(conf.getString(ConfigKey.LOGIN_USER.get()), "userFromSystemProperty");
	}

	@Test
	public void testLoadEnvironmentVariables() throws ConfigurationException {
		Configuration conf = new App().loadEnvironmentVariables();
		conf.getKeys().forEachRemaining(System.err::println);

		// 環境変数なのでSNAKE_CASE
		assertTrue(conf.containsKey(ConfigKey.BACKEND_URL.name()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_USER.name()));
		assertTrue(conf.containsKey(ConfigKey.LOGIN_PASSWORD.name()));
		assertEquals(conf.getString(ConfigKey.LOGIN_USER.name()), "userFromEnvironmentVariable");
	}

}
