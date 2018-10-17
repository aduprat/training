package org.linagora.event.sourcing.workflow.projection;

public class TaskVO {

	private final int taskId;
	private String name;
	private int numberOfMovements;
	private String currentAssignee;

	public TaskVO(int taskId, String name, int numberOfMovements, String currentAssignee) {
		this.taskId = taskId;
		this.name = name;
		this.numberOfMovements = numberOfMovements;
		this.currentAssignee = currentAssignee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfMovements() {
		return numberOfMovements;
	}

	public void setNumberOfMovements(int numberOfMovements) {
		this.numberOfMovements = numberOfMovements;
	}

	public String getCurrentAssignee() {
		return currentAssignee;
	}

	public void setCurrentAssignee(String currentAssignee) {
		this.currentAssignee = currentAssignee;
	}

	public int getTaskId() {
		return taskId;
	}

	@Override
	public String toString() {
		return "TaskVO [taskId=" + taskId + ", name=" + name + ", numberOfMovements=" + numberOfMovements
				+ ", currentAssignee=" + currentAssignee + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentAssignee == null) ? 0 : currentAssignee.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfMovements;
		result = prime * result + taskId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskVO other = (TaskVO) obj;
		if (currentAssignee == null) {
			if (other.currentAssignee != null)
				return false;
		} else if (!currentAssignee.equals(other.currentAssignee))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfMovements != other.numberOfMovements)
			return false;
		if (taskId != other.taskId)
			return false;
		return true;
	}

}
