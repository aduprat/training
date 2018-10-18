package org.linagora.event.sourcing;

import java.util.UUID;

public class AggregateId {

	public static class Factory {
		
		public AggregateId generate() {
			return new AggregateId(UUID.randomUUID());
		}
	}
	
	private final UUID randomUUID;
	
	private AggregateId(UUID randomUUID) {
		this.randomUUID = randomUUID;
	}

	public UUID getRandomUUID() {
		return randomUUID;
	}

	@Override
	public String toString() {
		return "Id [" + randomUUID + "]";
	}

}