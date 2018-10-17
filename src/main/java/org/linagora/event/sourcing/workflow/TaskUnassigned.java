package org.linagora.event.sourcing.workflow;

import java.util.Date;
import java.util.UUID;

public class TaskUnassigned implements TaskEvent {

	private final UUID taskId;
	private final Date eventDate;

	public TaskUnassigned(UUID taskId) {
		this.taskId = taskId;
		this.eventDate = new Date();
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public UUID taskId() {
		return taskId;
	}

	@Override
	public String toString() {
		return "TaskUnassigned [taskId=" + taskId + ", eventDate=" + eventDate + "]";
	}
}
