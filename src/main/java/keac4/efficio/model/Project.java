package keac4.efficio.model;

public class Project {

    private int projectId;
    private String name;
    private String description;
    private String startDate;
    private String deadline;
    private int expectedTime;

    public Project() {

    }

    public Project(String startDate, int projectId, String name, String description, String deadline, int expectedTime) {
        this.startDate = startDate;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.expectedTime = expectedTime;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }
}
