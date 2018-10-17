package org.linagora.event.sourcing.workflow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.Lists;

public class Task {

	private final UUID id;
	private String name;
	private Optional<User> assignee;

	public Task(String name) {
		this(name, Lists.newArrayList());
	}

	public Task(String name, List<Event> events) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.assignee = Optional.empty();
		events.stream()
			.forEach(this::apply);
	}

	public UUID getId() {
		return id;
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

	private void apply(Event event) {
		if (event instanceof TaskAssigned) {
			this.assignee = Optional.of(((TaskAssigned) event).getUser());
		} else if (event instanceof TaskUnassigned) {
			this.assignee = Optional.empty();
		} else if (event instanceof TaskCreated) {
			name = ((TaskCreated) event).getName();
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
}
