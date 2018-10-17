package org.linagora.event.sourcing.workflow;

import java.util.Date;

public class TaskAssigned implements Event {

	private final User user;

	private final TaskDescription task;

	private final Date eventDate;

	@Override
	public String toString() {
		return "AssignEvent [user=" + user + ", task=" + task + ", eventDate=" + eventDate + "]";
	}

	public TaskAssigned(User user, TaskDescription task) {
		this.user = user;
		this.task = task;
		this.eventDate = new Date();
	}

	public User getUser() {
		return user;
	}

	public TaskDescription getTask() {
		return task;
	}

	public Date getEventDate() {
		return eventDate;
	}

}
