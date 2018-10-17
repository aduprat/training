package org.linagora.event.sourcing.workflow;

public class AssignCommand implements Command {

	private final User user;

	public AssignCommand(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
