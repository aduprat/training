package org.linagora.event.sourcing.workflow.projection;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.linagora.event.sourcing.AggregateId;
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

	private Map<AggregateId, TaskVO> tasks;
	private Map<AggregateId, Map<Class<? extends TaskEvent>, Integer>> lastSequences;

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

	private void updateLastSequence(TaskEvent taskEvent, Class<? extends TaskEvent> clazz, String assignee) {
		AggregateId taskId = taskEvent.getTaskId();
		if (notProcessed(taskEvent, clazz)) {
			TaskVO taskVO = retrieveTaskVOOrCreate(taskId);
			taskVO.setNumberOfMovements(taskVO.getNumberOfMovements() + 1);
			taskVO.setCurrentAssignee(assignee);
			Map<Class<? extends TaskEvent>, Integer> map = lastSequences.get(taskEvent.getTaskId());
			map.put(clazz, taskEvent.getId());
		}
	}

	private TaskVO retrieveTaskVOOrCreate(AggregateId taskId) {
		TaskVO task = tasks.get(taskId);
		if (task == null) {
			task = new TaskVO(taskId, null, 0, null);
			tasks.put(taskId, task);
		}
		return task;
	}

	private boolean notProcessed(TaskEvent taskEvent, Class<? extends TaskEvent> clazz) {
		Map<Class<? extends TaskEvent>, Integer> map = lastSequences.get(taskEvent.getTaskId());
		if (map == null) {
			lastSequences.put(taskEvent.getTaskId(), Maps.newHashMap());
			return true;
		}
		Integer integer = map.get(clazz);
		if (integer == null) {
			return true;
		}
		if (taskEvent.getId() > integer) {
			return true;
		}
		return false;
//		return Optional.ofNullable(lastSequences.get(clazz))
//			.map(value -> value < taskEvent.getId())
//			.orElse(true);
	}

	public List<TaskVO> getTasks() {
		LOGGER.debug("Number of tasks {}", tasks.size());
		return tasks.values().stream().sorted(Comparator.comparing(TaskVO::getNumberOfMovements).reversed())
				.collect(Collectors.toList());
	}
}
