package keac4.efficio.model;

public class Task {
    private int taskId;
    private int subprojectId;
    private String name;
    private String description;
    private int expectedTime;

    public Task() {

    }

    public Task(int taskId, int subprojectId, String name, String description, int expectedTime) {
        this.taskId = taskId;
        this.subprojectId = subprojectId;
        this.name = name;
        this.description = description;
        this.expectedTime = expectedTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getSubprojectId() {
        return subprojectId;
    }

    public void setSubprojectId(int subprojectId) {
        this.subprojectId = subprojectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }
}
