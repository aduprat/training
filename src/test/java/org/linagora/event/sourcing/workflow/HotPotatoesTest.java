package org.linagora.event.sourcing.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.linagora.event.sourcing.workflow.projection.HotPotatoe;
import org.linagora.event.sourcing.workflow.projection.TaskVO;

public class HotPotatoesTest {

	@Test
	public void getTasksShouldReturnEmptyListWhenNoTasks() {
		HotPotatoe hotPotatoe = new HotPotatoe();
		List<TaskVO> tasks = hotPotatoe.getTasks();
		assertThat(tasks).isEmpty();
	}

	@Test
	public void getTasksShouldReturnSortedListWhenTasks() {
		HotPotatoe hotPotatoe = new HotPotatoe();
		
		Board board = new Board("myBoard");
		// First task 1 event
		CreateCommand createCommand = new CreateCommand("taskName");
		Optional<TaskCreated> createTask = board.createTask(createCommand);
		hotPotatoe.apply(createTask.get());
		
		Task task = createTask.get().task();
		User user = new User("name", UUID.randomUUID());
		AssignCommand assignCommand = new AssignCommand(user);
		Optional<TaskAssigned> assignEvent = task.assign(assignCommand);
		hotPotatoe.apply(assignEvent.get());

		// Second task 2 events
		CreateCommand createCommand2 = new CreateCommand("taskName2");
		Optional<TaskCreated> createTask2 = board.createTask(createCommand2);
		hotPotatoe.apply(createTask2.get());
		
		Task task2 = createTask2.get().task();
		AssignCommand assignCommand2 = new AssignCommand(user);
		Optional<TaskAssigned> assignEvent2 = task2.assign(assignCommand2);
		hotPotatoe.apply(assignEvent2.get());

		UnassignCommand unassignCommand = new UnassignCommand();
		Optional<TaskUnassigned> unassignEvent = task2.unassign(unassignCommand);
		hotPotatoe.apply(unassignEvent.get());

		List<TaskVO> tasks = hotPotatoe.getTasks();
		assertThat(tasks).hasSize(2)
			.containsExactly(new TaskVO(task2.getId(), "taskName2", 2, null),
					new TaskVO(task.getId(), "taskName", 1, user.getName()));
	}
}
