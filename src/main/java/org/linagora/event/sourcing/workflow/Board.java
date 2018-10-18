package org.linagora.event.sourcing.workflow;

import java.util.Optional;

import org.linagora.event.sourcing.Aggregate;
import org.linagora.event.sourcing.AggregateId;
import org.linagora.event.sourcing.TaskStorage;
import org.linagora.event.sourcing.workflow.Task.Factory;

public class Board implements Aggregate {

	private final AggregateId id;
	private String name;
	
	public Board(String name) {
		this.id = new AggregateId.Factory().generate();
		this.name = name;
	}

	@Override
	public AggregateId getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Optional<TaskCreated> createTask(CreateCommand command) {
		// FIXME
		Factory factory = new Task.Factory(new TaskStorage());
		TaskCreated event = new TaskCreated(factory.create(command.getName()));
		apply(event);
		return Optional.of(event);
	}

	private void apply(Event event) {
	}
}
