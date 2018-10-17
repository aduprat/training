package org.linagora.event.sourcing.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TaskTest {

	static final User USER = new User("name", UUID.randomUUID());
	static final TaskDescription TASK = new TaskDescription("taskName");
	static final AssignCommand COMMAND = new AssignCommand(USER);

	@Test
	public void assignShouldReturnATaskAssignedToCommandsUser() {
		Task testee = new Task(1, "taskName");
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event).isNotNull();
		assertThat(event.get().getUser()).isEqualTo(USER);
	}

	@Test
	public void assignShouldReturnAnEventOnGivenTask() {
		Task testee = new Task(1, "taskName");
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event.get().taskId()).isEqualTo(testee.getId());
	}

	@Test
	public void assignShouldReturnAnEventWithAValidDate() {
		Task testee = new Task(1, "taskName");
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event.get().getEventDate()).isNotNull();
	}

	@Test
	public void assignShouldReturnEmptyWhenOptionalMatches() {
		Task testee = new Task(1, "taskName");
		testee.assign(COMMAND);
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event).isEmpty();
	}

	@Test
	public void assignShouldReturnEmptyWhenUserAlreadyAssigned() {
		Task testee = new Task(1, "taskName");
		testee.assign(COMMAND);
		Optional<TaskAssigned> event = testee.assign(COMMAND);
		assertThat(event).isEmpty();
	}

	@Test
	public void assignShouldReturnEventWhenUserIsNotLastAssignee() {
		Task testee = new Task(1, "taskName");
		testee.assign(COMMAND);

		User user2 = new User("name2", UUID.randomUUID());
		AssignCommand command2 = new AssignCommand(user2);
		Optional<TaskAssigned> event = testee.assign(command2);
		assertThat(event.get().getUser()).isEqualTo(user2);
	}

	@Test
	public void unassignShouldReturnEmptyWhenNoUserAssigned() {
		Task testee = new Task(1, "taskName");
		Optional<TaskUnassigned> event = testee.unassign(new UnassignCommand());
		assertThat(event).isEmpty();
	}

	@Test
	public void unassignShouldReturnEventWhenUserAssigned() {
		Task testee = new Task(1, "taskName");
		testee.assign(COMMAND);
		Optional<TaskUnassigned> event = testee.unassign(new UnassignCommand());
		assertThat(event).isNotEmpty();
	}

	@Test
	public void projectionShouldBeInitializedWhenInputEventIsGiven() {
		int taskId = 1;
		Task testee = new Task(1, "taskName",
				Lists.newArrayList(new TaskAssigned(taskId, USER)));
		
		assertThat(testee.getAssignee()).isNotEmpty();
	}

	@Test
	public void projectionShouldBeInitializedWhenInputEventsAreGiven() {
		int taskId = 1;
		Task testee = new Task(1, "taskName",
				Lists.newArrayList(new TaskAssigned(taskId, USER), new TaskUnassigned(taskId)));
		
		assertThat(testee.getAssignee()).isEmpty();
	}

}
