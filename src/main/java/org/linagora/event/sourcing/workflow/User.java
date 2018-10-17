package org.linagora.event.sourcing.workflow;

import java.util.UUID;

public class User {

	private final String name;
	
	private final UUID id;

	public User(String name, UUID id) {
		this.name = name;
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", id=" + id + "]";
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
