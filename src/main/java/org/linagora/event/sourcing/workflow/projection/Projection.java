package org.linagora.event.sourcing.workflow.projection;

import org.linagora.event.sourcing.workflow.Event;

public interface Projection {

	void apply(Event event);

}
