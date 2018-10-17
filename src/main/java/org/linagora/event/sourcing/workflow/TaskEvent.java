package org.linagora.event.sourcing.workflow;

import java.util.Date;
import java.util.UUID;

public interface TaskEvent extends Event {

	UUID taskId();
	
	Date getEventDate();
}
