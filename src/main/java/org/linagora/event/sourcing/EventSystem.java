package org.linagora.event.sourcing;

import java.util.List;

import org.assertj.core.util.Lists;
import org.linagora.event.sourcing.workflow.Board;
import org.linagora.event.sourcing.workflow.Event;
import org.linagora.event.sourcing.workflow.projection.HotPotatoe;
import org.linagora.event.sourcing.workflow.projection.Projection;
import org.linagora.event.sourcing.workflow.projection.TaskVO;

public class EventSystem {

	private Board board;
	private HotPotatoe hotPotatoe;
	private PubSub pubsub;

	public EventSystem() {
		board = new Board("myBoard1");
		hotPotatoe = new HotPotatoe();
		pubsub = new PubSub();
		pubsub.sub(hotPotatoe);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void pub(Event event) {
		pubsub.pub(event);
	}
	
	public List<TaskVO> getTasks() {
		return hotPotatoe.getTasks();
	}
	
	private static class PubSub {
		
		private final List<Projection> projections;
		
		public PubSub() {
			this.projections = Lists.newArrayList();
		}
		
		public void pub(Event event) {
			for (Projection projection : projections) {
				projection.apply(event);
			}
		}
		
		public void sub(Projection projection) {
			projections.add(projection);
		}
	}
}
