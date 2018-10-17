package org.linagora.event.sourcing.workflow.projection;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.linagora.event.sourcing.workflow.Event;
import org.linagora.event.sourcing.workflow.Task;
import org.linagora.event.sourcing.workflow.TaskAssigned;
import org.linagora.event.sourcing.workflow.TaskCreated;
import org.linagora.event.sourcing.workflow.TaskEvent;
import org.linagora.event.sourcing.workflow.TaskUnassigned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class HotPotatoe implements Projection {

	private static final Logger LOGGER = LoggerFactory.getLogger(HotPotatoe.class);

	private Map<Integer, TaskVO> tasks;
	private Map<Class<? extends TaskEvent>, Integer> lastSequences;

	public HotPotatoe() {
		tasks = Maps.newHashMap();
		lastSequences = Maps.newHashMap();
	}

	@Override
	public void apply(Event event) {
		LOGGER.debug("Receive event {}", event);
		if (event instanceof TaskCreated) {
			Task task = ((TaskCreated) event).task();
			tasks.put(task.getId(), new TaskVO(task.getId(), task.getName(), 0, null));
		} else if (event instanceof TaskAssigned) {
			updateLastSequence((TaskAssigned) event, TaskAssigned.class, ((TaskAssigned) event).getUser().getName());
		} else if (event instanceof TaskUnassigned) {
			updateLastSequence((TaskUnassigned) event, TaskUnassigned.class, null);
		}
	}

	private void updateLastSequence(TaskEvent task, Class<? extends TaskEvent> clazz, String assignee) {
		int taskId = task.taskId();
		if (notProcessed(taskId, clazz)) {
			TaskVO taskVO = retrieveTaskVOOrCreate(taskId);
			taskVO.setNumberOfMovements(taskVO.getNumberOfMovements() + 1);
			taskVO.setCurrentAssignee(assignee);
			lastSequences.put(clazz, taskId);
		}
	}

	private TaskVO retrieveTaskVOOrCreate(int taskId) {
		TaskVO task = tasks.get(taskId);
		if (task == null) {
			task = new TaskVO(taskId, null, 0, null);
			tasks.put(taskId, task);
		}
		return task;
	}

	private boolean notProcessed(int taskId, Class<? extends TaskEvent> clazz) {
		return Optional.ofNullable(lastSequences.get(clazz))
			.map(value -> value < taskId)
			.orElse(true);
	}

	public List<TaskVO> getTasks() {
		LOGGER.debug("Number of tasks {}", tasks.size());
		return tasks.values().stream().sorted(Comparator.comparing(TaskVO::getNumberOfMovements).reversed())
				.collect(Collectors.toList());
	}
}
