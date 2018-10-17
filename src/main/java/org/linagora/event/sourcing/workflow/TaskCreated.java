package org.linagora.event.sourcing.workflow;

import java.util.Date;

public class TaskCreated implements Event {

	private final Task task;
	private final Date eventDate;

	public TaskCreated(Task task) {
		this.task = task;
		this.eventDate = new Date();
	}

	public Task task() {
		return task;
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public String toString() {
		return "TaskCreated [task=" + task + ", eventDate=" + eventDate + "]";
	}
}
