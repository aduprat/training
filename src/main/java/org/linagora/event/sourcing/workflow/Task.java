package org.linagora.event.sourcing.workflow;

import java.util.List;
import java.util.Optional;

import org.linagora.event.sourcing.Aggregate;
import org.linagora.event.sourcing.AggregateId;
import org.linagora.event.sourcing.TaskStorage;

public class Task implements Aggregate {

	public static class Factory {

		private final TaskStorage storage;

		public Factory(TaskStorage storage) {
			this.storage = storage;
		}

		public Task load(AggregateId id) {
			return new Task(storage, id, storage.retrieve(id));
		}

		public Task load(AggregateId id, List<TaskEvent> events) {
			return new Task(storage, id, events);
		}

		public Task create(String name) {
			// FIXME : TaskCreated event ?
			return new Task(storage, name);
		}
	}

	private final AggregateId id;
	private int sequence;
	private String name;
	private Optional<User> assignee;
	private TaskStorage storage;

	private Task(TaskStorage storage, AggregateId id, List<TaskEvent> events) {
		this.id = id;
		this.sequence = 0;
		this.name = null;
		this.assignee = Optional.empty();
		this.storage = storage;
		processEvents(events);
	}

	private Task(TaskStorage storage, String name) {
		this.id = new AggregateId.Factory().generate();
		this.sequence = 0;
		this.name = name;
		this.assignee = Optional.empty();
		this.storage = storage;
	}

	private void processEvents(List<TaskEvent> events) {
		events.stream().forEach(this::apply);
	}

	@Override
	public AggregateId getId() {
		return id;
	}

	public int getSequence() {
		return sequence;
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
		TaskAssigned event = new TaskAssigned(id, sequence++, command.getUser());
		applyThenPersist(event);
		return Optional.of(event);
	}

	private void applyThenPersist(TaskEvent event) {
		this.apply(event);
		this.storage.store(event);
	}

	private void apply(TaskEvent event) {
		if (event instanceof TaskAssigned) {
			this.assignee = Optional.of(((TaskAssigned) event).getUser());
		} else if (event instanceof TaskUnassigned) {
			this.assignee = Optional.empty();
		} else if (event instanceof TaskCreated) {
			name = ((TaskCreated) event).task().getName();
		}
		this.sequence = event.getId();
	}

	public Optional<TaskUnassigned> unassign(UnassignCommand command) {
		if (!this.assignee.isPresent()) {
			return Optional.empty();
		}
		TaskUnassigned event = new TaskUnassigned(id, sequence++);
		applyThenPersist(event);
		return Optional.of(event);
	}

	public Optional<User> getAssignee() {
		return assignee;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", sequence=" + sequence + ", name=" + name + "]";
	}

}
