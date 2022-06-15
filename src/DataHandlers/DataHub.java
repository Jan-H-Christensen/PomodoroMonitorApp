package DataHandlers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * is observing the Employee and Pomodoro object for changes of the variables
 *
 * @author  Jan Christensen
 * @version 1.0
 * @since 06.06.2022
 */

public class DataHub {

    //checks for changes in the pomodoro
    private static IntegerProperty pomodoroIp = new SimpleIntegerProperty();
    public static IntegerProperty pomodoroIpProperty(){return pomodoroIp;}

    public static int getPomodoroIp() {
        return pomodoroIp.get();
    }
    public static void setPomodoroIp(int pomodoroIp) {
        DataHub.pomodoroIp.set(pomodoroIp);
    }

    //check for changes of status in the Employee object
    public static void setEmployeeIp(int employeeIp) {
        DataHub.employeeIp.set(employeeIp);
    }
    private static IntegerProperty employeeIp = new SimpleIntegerProperty();

    public static IntegerProperty ipProperty() {
        return employeeIp;
    }
    public static int getEmployeeIp() {
        return employeeIp.get();
    }
}