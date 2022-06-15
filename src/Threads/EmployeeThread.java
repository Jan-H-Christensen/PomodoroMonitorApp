package Threads;

import Application.Main;
import DataHandlers.DBHandler;
import DataHandlers.DataHub;
import DatabaseObjects.Employee;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;

/**
 *  creates my own Task for check the status of the employee via a Runnable
 * and pass it to a thread
 *
 * @author Jan Christensen
 * @version 1.0
 * @since 08.06.2022
 */
public class EmployeeThread extends Task {

    @Override
    protected Object call() throws Exception {

        while (true) {
            try {
                Thread.sleep(10000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Employee> checkEmployees = DBHandler.getAllEmployee();
                        for (Employee employee : Main.getEmployees()) {
                            for (Employee checkEmployee : checkEmployees) {
                                if (employee.getEmployeeID() == checkEmployee.getEmployeeID() && employee.getStatus() != checkEmployee.getStatus()) {
                                    if (DataHub.getEmployeeIp() == 1) {
                                        DataHub.setEmployeeIp(0);
                                    } else {
                                        DataHub.setEmployeeIp(1);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


