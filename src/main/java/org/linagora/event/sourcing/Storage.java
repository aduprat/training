package org.linagora.event.sourcing;

import java.util.List;

import org.linagora.event.sourcing.workflow.Event;

public interface Storage<E extends Event> {

	void store(E event);
	
	List<E> retrieve(AggregateId id);
}
