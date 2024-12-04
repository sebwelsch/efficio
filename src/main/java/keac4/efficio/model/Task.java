package keac4.efficio.model;

public class Task {
    private int taskId;
    private String name;
    private String description;
    private int expectedTime;
    private int subprojectId;

    public Task() {

    }

    public Task(int taskId, String name, String description, int expectedTime, int subprojectId) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.expectedTime = expectedTime;
        this.subprojectId = subprojectId;
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
