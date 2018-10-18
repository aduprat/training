package org.linagora.event.sourcing.workflow;

import java.util.Date;

import org.linagora.event.sourcing.AggregateId;

public interface TaskEvent extends Event {

	AggregateId getTaskId();
	
	int getId();
	
	Date getEventDate();
}
