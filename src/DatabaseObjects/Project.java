package DatabaseObjects;

public class Project {
    private int taskID;
    private int estimateTime;
    private int numberWorkOn;
    private String taskName;
    private String status;
    /**
     * creates a Project object to get the needed variable about the current project
     *
     * @author Jan Christensen
     * @version 1.0
     * @since 06.06.2022
     */
    public Project(int taskID, int estimateTime, int numberWorkOn, String taskName, String status) {
        this.taskID = taskID;
        this.estimateTime = estimateTime;
        this.numberWorkOn = numberWorkOn;
        this.taskName = taskName;
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    public int getNumberWorkOn() {
        return numberWorkOn;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getStatus() {
        return status;
    }
}
