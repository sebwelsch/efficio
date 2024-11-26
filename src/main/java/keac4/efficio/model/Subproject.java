package keac4.efficio.model;

public class Subproject {
    private int subprojectId;
    private int projectId;
    private String name;
    private String description;
    private String startDate;
    private String deadline;
    private int expectedTime;

    public Subproject() {

    }

    public Subproject(int projectId, int subprojectId, String name, String description, String startDate, String deadline, int expectedTime) {
        this.projectId = projectId;
        this.subprojectId = subprojectId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline;
        this.expectedTime = expectedTime;
    }

    public String getStartDate() {
        return startDate;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getSubprojectId() {
        return subprojectId;
    }

    public void setSubprojectId(int subprojectId) {
        this.subprojectId = subprojectId;
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
