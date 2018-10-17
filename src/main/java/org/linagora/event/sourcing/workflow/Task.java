package org.linagora.event.sourcing.workflow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Task {

	private List<Event> events;
	private Optional<TaskAssigned> lastEvent;
	private final UUID id;

	public Task(String name) {
		this.events = new ArrayList<>();
		this.id = UUID.randomUUID();
		computeProjection();
	}

	public UUID getId() {
		return id;
	}

	public Optional<TaskAssigned> assign(AssignCommand command) {
		if (lastEvent.isPresent()) {
			if (lastEvent.get().getUser().equals(command.getUser())) {
				return Optional.empty();
			}
		}
		TaskAssigned assignEvent = new TaskAssigned(id, command.getUser());
		events.add(assignEvent);
		computeProjection();
		return Optional.of(assignEvent);		
	}

	private void computeProjection() {
		lastEvent = events.stream()
				.filter(TaskAssigned.class::isInstance)
				.map(TaskAssigned.class::cast)
				.sorted(Comparator.comparing(TaskAssigned::getEventDate).reversed())
				.findFirst();
	}
}
