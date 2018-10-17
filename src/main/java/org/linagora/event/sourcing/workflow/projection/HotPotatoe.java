package org.linagora.event.sourcing.workflow.projection;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.linagora.event.sourcing.workflow.TaskEvent;
import org.linagora.event.sourcing.workflow.TaskAssigned;
import org.linagora.event.sourcing.workflow.TaskCreated;
import org.linagora.event.sourcing.workflow.TaskUnassigned;

import com.google.common.collect.Maps;


public class HotPotatoe implements Projection {

	private Map<UUID, TaskVO> tasks;
	
	public HotPotatoe() {
		tasks = Maps.newHashMap();
	}
	
	public void apply(TaskEvent event) {
		if (event instanceof TaskCreated) {
			tasks.put(event.taskId(), new TaskVO(event.taskId(), ((TaskCreated) event).getName(), 0, null));
		} else if (event instanceof TaskAssigned) {
			TaskVO task = tasks.get(event.taskId());
			if (task == null) {
				task = new TaskVO(event.taskId(), null, 0, null);
				tasks.put(event.taskId(), task);
			}
			task.setNumberOfMovements(task.getNumberOfMovements() + 1);
			task.setCurrentAssignee(((TaskAssigned) event).getUser().getName());
		} else if (event instanceof TaskUnassigned) {
			TaskVO task = tasks.get(event.taskId());
			if (task == null) {
				task = new TaskVO(event.taskId(), null, 0, null);
				tasks.put(event.taskId(), task);
			}
			task.setNumberOfMovements(task.getNumberOfMovements() + 1);
			task.setCurrentAssignee(null);
		}
	}
	
	public List<TaskVO> getTasks() {
		return tasks.values()
				.stream()
				.sorted(Comparator.comparing(TaskVO::getNumberOfMovements).reversed())
				.collect(Collectors.toList());
	}
}
