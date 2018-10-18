package org.linagora.event.sourcing.workflow;

import java.util.Date;

import org.linagora.event.sourcing.AggregateId;

public class TaskUnassigned implements TaskEvent {

	private final AggregateId taskId;
	private final int id;
	private final Date eventDate;

	public TaskUnassigned(AggregateId taskId, int id) {
		this.taskId = taskId;
		this.id = id;
		this.eventDate = new Date();
	}

	@Override
	public AggregateId getTaskId() {
		return taskId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "TaskUnassigned [taskId=" + taskId + ", id=" + id + ", eventDate=" + eventDate + "]";
	}
}
