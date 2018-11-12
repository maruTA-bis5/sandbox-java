package net.bis5.sandbox;

enum ConfigKey {
	BACKEND_URL("backend.url"),
	LOGIN_USER("login.user"),
	LOGIN_PASSWORD("login.password");
	
	private final String key;
	ConfigKey(String key) {
		this.key = key;
	}
	public String get() {
		return key;
	}
}
