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
		List<TaskEvent> list = storage.get(event.getTaskId());
		if (list == null) {
			list = Lists.newArrayList();
			storage.put(event.getTaskId(), list);
		}
		// TODO Do not store two events with same id !
		list.add(event);
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
