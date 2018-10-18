package org.linagora.event.sourcing.workflow;

import java.util.Date;

import org.linagora.event.sourcing.AggregateId;

public class TaskAssigned implements TaskEvent {

	private final AggregateId taskId;
	private final int id;
	private final User user;
	private final Date eventDate;

	public TaskAssigned(AggregateId taskId, int id, User user) {
		this.taskId = taskId;
		this.id = id;
		this.user = user;
		this.eventDate = new Date();
	}

	@Override
	public AggregateId getTaskId() {
		return taskId;
	}

	@Override
	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Date getEventDate() {
		return eventDate;
	}

	@Override
	public String toString() {
		return "TaskAssigned [taskId=" + taskId + ", id=" + id + ", user=" + user + ", eventDate=" + eventDate + "]";
	}
}
