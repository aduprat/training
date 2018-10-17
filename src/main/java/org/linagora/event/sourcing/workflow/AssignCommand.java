package org.linagora.event.sourcing.workflow;

public class AssignCommand implements Command {

	private final User user;

	private final TaskDescription task;

	public AssignCommand(User user, TaskDescription task) {
		this.user = user;
		this.task = task;
	}

	public User getUser() {
		return user;
	}

	public TaskDescription getTask() {
		return task;
	}

}
