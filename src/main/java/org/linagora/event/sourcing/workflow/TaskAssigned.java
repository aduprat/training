package org.linagora.event.sourcing.workflow;

import java.util.Date;
import java.util.UUID;

public class TaskAssigned implements Event {

	private final UUID taskId;
	private final User user;
	private final Date eventDate;

	public TaskAssigned(UUID taskId, User user) {
		this.taskId = taskId;
		this.user = user;
		this.eventDate = new Date();
	}

	@Override
	public UUID taskId() {
		return taskId;
	}

	public User getUser() {
		return user;
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public String toString() {
		return "TaskAssigned [user=" + user + ", eventDate=" + eventDate + "]";
	}
}
