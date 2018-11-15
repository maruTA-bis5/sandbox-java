package net.bis5.sandbox;

import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.ex.*;
import org.apache.commons.configuration2.builder.*;
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

	public Configuration loadYaml(String yamlFile) throws ConfigurationException {
		BasicConfigurationBuilder<YAMLConfiguration> builder = new FileBasedConfigurationBuilder<>(YAMLConfiguration.class) //
			.configure(new Parameters().hierarchical() //
					.setFileName(yamlFile) //
					);

		return builder.getConfiguration();
	}

	public Configuration loadSystemProperties() throws ConfigurationException {
		return new SystemConfiguration();
	}

	public Configuration loadEnvironmentVariables() throws ConfigurationException {
		return new EnvironmentConfiguration();
	}
}
