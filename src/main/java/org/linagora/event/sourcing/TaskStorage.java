package org.linagora.event.sourcing;

import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.linagora.event.sourcing.workflow.TaskEvent;

import com.google.common.collect.Maps;

public class TaskStorage implements Storage<TaskEvent> {

	private Map<AggregateId, List<TaskEvent>> storage;
	
	public TaskStorage() {
		super();
		this.storage = Maps.newHashMap();
	}

	@Override
	public void store(TaskEvent event) {
		List<TaskEvent> events = storage.get(event.getTaskId());
		if (events == null) {
			events = Lists.newArrayList();
			storage.put(event.getTaskId(), events);
		}
		if (events.contains(event)) {
			throw new RuntimeException("Duplicate event");
		}
		events.add(event);
	}

	@Override
	public List<TaskEvent> retrieve(AggregateId id) {
		List<TaskEvent> events = storage.get(id);
		if (events == null) {
			return Lists.newArrayList();
		}
		return events;
	}

}
