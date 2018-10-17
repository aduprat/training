package org.linagora.event.sourcing.workflow.projection;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.linagora.event.sourcing.workflow.Event;
import org.linagora.event.sourcing.workflow.Task;
import org.linagora.event.sourcing.workflow.TaskAssigned;
import org.linagora.event.sourcing.workflow.TaskCreated;
import org.linagora.event.sourcing.workflow.TaskUnassigned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;


public class HotPotatoe implements Projection {

	private static final Logger LOGGER = LoggerFactory.getLogger(HotPotatoe.class);
	
	private Map<Integer, TaskVO> tasks;
	private Map<Class<Event>, Integer> lastSequences; 
	
	public HotPotatoe() {
		tasks = Maps.newHashMap();
		lastSequences = Maps.newHashMap();
	}
	
	public void apply(Event event) {
		LOGGER.debug("Receive event {}", event);
		if (event instanceof TaskCreated) {
			Task task = ((TaskCreated) event).task();
			tasks.put(task.getId(), new TaskVO(task.getId(), task.getName(), 0, null));
		} else if (event instanceof TaskAssigned) {
			int taskId = ((TaskAssigned) event).taskId();
			if (notProcessed(taskId, TaskAssigned.class)) {
				TaskVO task = tasks.get(taskId);
				if (task == null) {
					task = new TaskVO(taskId, null, 0, null);
					tasks.put(taskId, task);
				}
				task.setNumberOfMovements(task.getNumberOfMovements() + 1);
				task.setCurrentAssignee(((TaskAssigned) event).getUser().getName());
			}
		} else if (event instanceof TaskUnassigned) {
			int taskId = ((TaskUnassigned) event).taskId();
			if (notProcessed(taskId, TaskAssigned.class)) {
				TaskVO task = tasks.get(taskId);
				if (task == null) {
					task = new TaskVO(taskId, null, 0, null);
					tasks.put(taskId, task);
				}
				task.setNumberOfMovements(task.getNumberOfMovements() + 1);
				task.setCurrentAssignee(null);
			}
		}
	}
	
	private boolean notProcessed(int taskId, Class<TaskAssigned> clazz) {
		if (lastSequences.get(clazz) < taskId) {
			return true;
		}
		return false;
	}

	public List<TaskVO> getTasks() {
		LOGGER.debug("Number of tasks {}", tasks.size());
		return tasks.values()
				.stream()
				.sorted(Comparator.comparing(TaskVO::getNumberOfMovements).reversed())
				.collect(Collectors.toList());
	}
}
