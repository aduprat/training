package org.linagora.event.sourcing.workflow;

import java.util.Date;

public interface TaskEvent extends Event {

	int taskId();
	
	Date getEventDate();
}
