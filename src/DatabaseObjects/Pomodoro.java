package DatabaseObjects;
/**
 * creates a Pomodoro object to get the needed variable about the current pomodoro
 *
 * @author Jan Christensen
 * @version 1.0
 * @since 06.06.2022
 */
public class Pomodoro {
    private int pomodoroID;
    private int workTime;
    private int BreakTime;

    public Pomodoro(int pomodoroID, int workTime, int breakTime) {
        this.pomodoroID = pomodoroID;
        this.workTime = workTime;
        this.BreakTime = breakTime;
    }

    public int getPomodoroID() {
        return pomodoroID;
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getBreakTime() {
        return BreakTime;
    }
}
