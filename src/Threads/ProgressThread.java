package Threads;

import Application.Main;
import DataHandlers.DBHandler;
import DataHandlers.DataHub;
import DatabaseObjects.Pomodoro;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;

/**
 *  creates my own Task for check the pomodoro  via a Runnable
 * and pass it to a thread
 *
 * @author Jan Christensen
 * @version 1.0
 * @since 08.06.2022
 */
public class ProgressThread extends Task {
    @Override
    protected Object call() throws Exception {

        while (true){

            Thread.sleep(30000);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Pomodoro> pomodoros = DBHandler.getPomodoro();

                    for (Pomodoro pomodoro : Main.getPomodoros()) {
                        for (Pomodoro checkPomodoro : pomodoros) {
                            if(pomodoro.getBreakTime() == checkPomodoro.getBreakTime() && pomodoro.getWorkTime() == checkPomodoro.getWorkTime()){
                                if(DataHub.getPomodoroIp() == 1){
                                    DataHub.setPomodoroIp(0);
                                }else {
                                    DataHub.setPomodoroIp(1);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
