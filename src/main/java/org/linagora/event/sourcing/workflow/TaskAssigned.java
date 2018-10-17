package org.linagora.event.sourcing.workflow;

import java.util.Date;

public class TaskAssigned implements TaskEvent {

	private final int taskId;
	private final User user;
	private final Date eventDate;

	public TaskAssigned(int id, User user) {
		this.taskId = id;
		this.user = user;
		this.eventDate = new Date();
	}

	@Override
	public int taskId() {
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
