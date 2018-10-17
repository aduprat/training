package org.linagora.event.sourcing.workflow;

public class TaskDescription {

	private final String name;

	public TaskDescription(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
