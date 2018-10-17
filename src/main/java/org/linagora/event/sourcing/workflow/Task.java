package org.linagora.event.sourcing.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Task {

	private List<Event> events;
	private Optional<TaskAssigned> lastEvent;
	private final UUID id;
	private String name;
	private Optional<User> assignee;

	public Task(String name) {
		this.events = new ArrayList<>();
		this.id = UUID.randomUUID();
		this.name = name;
		this.assignee = Optional.empty();
//		computeProjection();
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
		TaskAssigned assignEvent = new TaskAssigned(id, command.getUser());
		apply(assignEvent);
		return Optional.of(assignEvent);
	}

	private void apply(Event event) {
		events.add(event);
		if (event instanceof TaskAssigned) {
			this.assignee = Optional.of(((TaskAssigned) event).getUser());
		} else if (event instanceof TaskUnassigned) {
			this.assignee = Optional.empty();
		}
	}

//	public Optional<TaskUnassigned> unassign(UnassignCommand command) {
//	}
}
