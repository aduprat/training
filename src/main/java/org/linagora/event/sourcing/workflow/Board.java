package org.linagora.event.sourcing.workflow;

import java.util.Optional;
import java.util.UUID;

public class Board {

	private final UUID id;
	private String name;
	
	private int sequence = 0;
	
	public Board(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
	}

	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Optional<TaskCreated> createTask(CreateCommand command) {
		TaskCreated event = new TaskCreated(new Task(sequence++, command.getName()));
		apply(event);
		return Optional.of(event);
	}

	private void apply(Event event) {
	}
}
