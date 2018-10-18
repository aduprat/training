package org.linagora.event.sourcing.workflow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.linagora.event.sourcing.AggregateId;
import org.linagora.event.sourcing.TaskStorage;

public class TaskStorageTest {

	@Test
	public void retrieveShouldReturnStoredEvents() {
		TaskStorage testee = new TaskStorage();
		
		AggregateId taskId = new AggregateId.Factory().generate();
		User user = new User("userName", UUID.randomUUID());
		TaskEvent event = new TaskAssigned(taskId, 0, user);
		TaskEvent event2 = new TaskUnassigned(taskId, 1);
		TaskEvent event3 = new TaskAssigned(taskId, 2, user);
		testee.store(event);
		testee.store(event2);
		testee.store(event3);
		
		List<TaskEvent> events = testee.retrieve(taskId);
		assertThat(events)
			.containsOnly(event, event2, event3);
	}

	@Test
	public void retrieveShouldReturnStoredEventsWhenMultipleTasks() {
		TaskStorage testee = new TaskStorage();
		
		AggregateId taskId = new AggregateId.Factory().generate();
		User user = new User("userName", UUID.randomUUID());
		TaskEvent event = new TaskAssigned(taskId, 0, user);
		TaskEvent event2 = new TaskUnassigned(taskId, 1);
		AggregateId taskId2 = new AggregateId.Factory().generate();
		TaskEvent event3 = new TaskAssigned(taskId2, 2, user);
		testee.store(event);
		testee.store(event2);
		testee.store(event3);
		
		List<TaskEvent> events = testee.retrieve(taskId2);
		assertThat(events)
			.containsOnly(event3);
	}

	@Test
	public void storeShouldThrowWhenStoringTwoTimesTheSameEvent() {
		TaskStorage testee = new TaskStorage();
		
		AggregateId taskId = new AggregateId.Factory().generate();
		User user = new User("userName", UUID.randomUUID());
		TaskEvent event = new TaskAssigned(taskId, 0, user);
		testee.store(event);
		assertThatThrownBy(() -> testee.store(event))
			.isInstanceOf(RuntimeException.class);
	}
}
