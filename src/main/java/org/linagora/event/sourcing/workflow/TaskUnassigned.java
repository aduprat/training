package org.linagora.event.sourcing.workflow;

import java.util.Date;

public class TaskUnassigned implements TaskEvent {

	private final int taskId;
	private final Date eventDate;

	public TaskUnassigned(int taskId) {
		this.taskId = taskId;
		this.eventDate = new Date();
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public int taskId() {
		return taskId;
	}

	@Override
	public String toString() {
		return "TaskUnassigned [taskId=" + taskId + ", eventDate=" + eventDate + "]";
	}
}
