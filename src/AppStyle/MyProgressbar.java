
package AppStyle;
/**
 * MyProgressbar creates an object that initialize the necessary data for creating
 * a progressbar label
 *
 * @author Jan Christensen
 * @version 1.0
 * @since 14.06.2022
 */
public class MyProgressbar {

    private int amountOfPomodoro;
    private int workTime;
    private int breakTime;


    public MyProgressbar(int amountOfPomodoro, int workTime, int breakTime) {
        this.amountOfPomodoro = amountOfPomodoro;
        this.workTime = workTime;
        this.breakTime = breakTime;
    }

    public int getAmountOfPomodoro() {
        return amountOfPomodoro;
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getBreakTime() {
        return breakTime;
    }
}
