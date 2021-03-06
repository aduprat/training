package org.linagora.event.sourcing.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.linagora.event.sourcing.AggregateId;
import org.linagora.event.sourcing.TaskStorage;
import org.linagora.event.sourcing.workflow.Task.Factory;

import com.google.common.collect.Lists;

public class TaskTest {

	static final User USER = new User("name", UUID.randomUUID());
	static final TaskDescription TASK = new TaskDescription("taskName");
	static final AssignCommand COMMAND = new AssignCommand(USER);
	static final Factory FACTORY = new Task.Factory(new TaskStorage());

	@Test
	public void assignShouldReturnATaskAssignedToCommandsUser() {
		Task testee = FACTORY.create("taskName");
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event).isNotNull();
		assertThat(event.get().getUser()).isEqualTo(USER);
	}

	@Test
	public void assignShouldReturnAnEventOnGivenTask() {
		Task testee = FACTORY.create("taskName");
		assertThat(testee.getName()).isEqualTo("taskName");
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event.get().getId()).isEqualTo(testee.getSequence());
	}

	@Test
	public void assignShouldReturnAnEventWithAValidDate() {
		Task testee = FACTORY.create("taskName");
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event.get().getEventDate()).isNotNull();
	}

	@Test
	public void assignShouldReturnEmptyWhenOptionalMatches() {
		Task testee = FACTORY.create("taskName");
		testee.assign(COMMAND);
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event).isEmpty();
	}

	@Test
	public void assignShouldReturnEmptyWhenUserAlreadyAssigned() {
		Task testee = FACTORY.create("taskName");
		testee.assign(COMMAND);
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event).isEmpty();
	}

	@Test
	public void assignShouldReturnEventWhenUserIsNotLastAssignee() {
		Task testee = FACTORY.create("taskName");
		testee.assign(COMMAND);

		User user2 = new User("name2", UUID.randomUUID());
		AssignCommand command2 = new AssignCommand(user2);
		Optional<TaskAssigned> event = testee.assign(command2);
		assertThat(event.get().getUser()).isEqualTo(user2);
	}

	@Test
	public void unassignShouldReturnEmptyWhenNoUserAssigned() {
		Task testee = FACTORY.create("taskName");
		Optional<TaskUnassigned> event = testee.unassign(new UnassignCommand());
		assertThat(event).isEmpty();
	}

	@Test
	public void unassignShouldReturnEventWhenUserAssigned() {
		Task testee = FACTORY.create("taskName");
		testee.assign(COMMAND);
		Optional<TaskUnassigned> event = testee.unassign(new UnassignCommand());
		assertThat(event).isNotEmpty();
	}

	@Test
	public void projectionShouldBeInitializedWhenInputEventIsGiven() {
		AggregateId id = new AggregateId.Factory().generate();
		Task testee = FACTORY.load(id,
				Lists.newArrayList(new TaskAssigned(id, 0, USER)));
		assertThat(testee.getAssignee()).isNotEmpty();
	}

	@Test
	public void projectionShouldBeInitializedWhenInputEventsAreGiven() {
		AggregateId id = new AggregateId.Factory().generate();
		Task testee = FACTORY.load(id,
				Lists.newArrayList(new TaskAssigned(id, 0, USER), new TaskUnassigned(id, 1)));
		assertThat(testee.getAssignee()).isEmpty();
	}

}
