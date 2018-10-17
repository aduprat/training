package org.linagora.event.sourcing.workflow;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

public class Task {

	private final int id;
	private String name;
	private Optional<User> assignee;

	public Task(int id, String name) {
		this(id, name, Lists.newArrayList());
	}

	public Task(int id, String name, List<TaskEvent> events) {
		this.id = id;
		this.name = name;
		this.assignee = Optional.empty();
		events.stream()
			.forEach(this::apply);
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Optional<TaskAssigned> assign(AssignCommand command) {
		if (this.assignee.isPresent()) {
			if (this.assignee.get().equals(command.getUser())) {
				return Optional.empty();
			}
		}
		TaskAssigned event = new TaskAssigned(id, command.getUser());
		apply(event);
		return Optional.of(event);
	}

	private void apply(TaskEvent event) {
		if (event instanceof TaskAssigned) {
			this.assignee = Optional.of(((TaskAssigned) event).getUser());
		} else if (event instanceof TaskUnassigned) {
			this.assignee = Optional.empty();
		} else if (event instanceof TaskCreated) {
			name = ((TaskCreated) event).task().getName();
		}
	}

	public Optional<TaskUnassigned> unassign(UnassignCommand command) {
		if (!this.assignee.isPresent()) {
			return Optional.empty();
		}
		TaskUnassigned event = new TaskUnassigned(id);
		apply(event);
		return Optional.of(event);
	}

	public Optional<User> getAssignee() {
		return assignee;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + "]";
	}
	
}
