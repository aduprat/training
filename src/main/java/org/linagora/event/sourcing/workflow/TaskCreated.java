package org.linagora.event.sourcing.workflow;

import java.util.Date;
import java.util.UUID;

public class TaskCreated implements Event {

	private final UUID taskId;
	private final String name;
	private final Date eventDate;

	public TaskCreated(UUID taskId, String name) {
		this.taskId = taskId;
		this.name = name;
		this.eventDate = new Date();
	}

	@Override
	public UUID taskId() {
		return taskId;
	}

	public String getName() {
		return name;
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public String toString() {
		return "TaskCreated [name=" + name + ", eventDate=" + eventDate + "]";
	}
}
