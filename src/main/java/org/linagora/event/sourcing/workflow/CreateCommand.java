package org.linagora.event.sourcing.workflow;

public class CreateCommand implements Command {

	private final String name;

	public CreateCommand(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
