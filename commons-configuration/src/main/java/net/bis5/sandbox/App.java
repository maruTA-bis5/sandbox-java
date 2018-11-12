package net.bis5.sandbox;

import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.ex.*;
import org.apache.commons.configuration2.builder.fluent.*;

public class App {
	public Configuration loadIni(String iniFile) throws ConfigurationException {
		Configurations configurations = new Configurations();

		return configurations.ini(iniFile);
	}

	public Configuration loadProperties(String propertyFile) throws ConfigurationException {
		Configurations configurations = new Configurations();

		return configurations.properties(propertyFile);
	}
}
