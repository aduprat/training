package org.linagora.event.sourcing.workflow;

import java.util.List;
import java.util.Optional;

import org.linagora.event.sourcing.Aggregate;
import org.linagora.event.sourcing.AggregateId;

import com.google.common.collect.Lists;

public class Task implements Aggregate {

	public static Task withEvents(AggregateId id, List<TaskEvent> events) {
		Task task = new Task(id);
		task.processEvents(events);
		return task;
	}
	
	private final AggregateId id;
	private int sequence;
	private String name;
	private Optional<User> assignee;

	private Task(AggregateId id) {
		this.id = id;
		this.sequence = 0;
	}
	
	public Task(String name) {
		this(name, Lists.newArrayList());
	}

	public Task(String name, List<TaskEvent> events) {
		this.id = new AggregateId.Factory().generate();
		this.sequence = 0;
		this.name = name;
		this.assignee = Optional.empty();
		processEvents(events);
	}

	private void processEvents(List<TaskEvent> events) {
		events.stream()
			.forEach(this::apply);
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
		this.sequence = event.getId();
	}

	public Optional<TaskUnassigned> unassign(UnassignCommand command) {
		if (!this.assignee.isPresent()) {
			return Optional.empty();
		}
		TaskUnassigned event = new TaskUnassigned(id, sequence++);
		apply(event);
		return Optional.of(event);
	}

	public Optional<User> getAssignee() {
		return assignee;
	}

	@Override
	public String toString() {
		return "Task [sequence=" + sequence + ", name=" + name + "]";
	}
	
}
