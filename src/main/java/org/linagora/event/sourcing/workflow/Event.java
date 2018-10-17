package org.linagora.event.sourcing.workflow;

import java.util.Date;
import java.util.UUID;

public interface Event {

	UUID taskId();
	
	Date getEventDate();
}
