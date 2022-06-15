package Application;

import AppStyle.MyProgressbar;
import DataHandlers.DBHandler;
import DataHandlers.DataHub;
import Threads.EmployeeThread;
import DatabaseObjects.*;
import Threads.ProgressThread;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * starts the application and initializing the work monitor
 *
 * @author Jan Christensen
 * @versio 1.0
 * @since 06.06.2022
 */

public class Main extends Application {
    private Pane pane;
    private Scene scene;
    private Label user, progressLabel, breakLabel, arrowLabel, headline, labelLine, departmentLabel;
    private ComboBox<String> department = new ComboBox<>();
    private final int START_COORDINATE=75;
    private final int USER_LABEL_X_COORDINATE = 50;
    private final int Y_COORDINATE = 60;
    private final int PROGRESS_LABEL_X_COORDINATE = 200;
    private final int TIME_MULTIPLIER = 10;
    private final int PROGRESSBAR_HEIGHT =40;
    private final int AVOID_ZERO_MULTIPLIER = 1;
    private final double GABE_VARIABLE = 17.5;
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static ArrayList<Pomodoro> pomodoros = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {

        pane = new Pane();
        scene = new Scene(pane,1200,700);
        scene.getStylesheets().add("AppStyle/MonitorStyle.css");

        employees = DBHandler.getAllEmployee();
        pomodoros = DBHandler.getPomodoro();

        //inizialize the departments to a dropdown / ComboBox and adds them to a observableList
        ObservableList<String> observableList = FXCollections.observableArrayList();

        for(Employee employee: getEmployees()){
            //cointains method checks for duplicates
            if(!observableList.contains(employee.getDepartment())){
                observableList.add(employee.getDepartment());
            }
        }

        departmentLabel = new Label("Department");
        departmentLabel.setId("departmentLabel");
        departmentLabel.setLayoutX(50);
        departmentLabel.setLayoutY(25);


        department.setItems(observableList);
        department.setId("department");
        department.setLayoutX(50);
        department.setLayoutY(50);
        department.toFront();
        department.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                createUserLabel();
            }
        });

        pane.getChildren().addAll(departmentLabel,department);

        stage.setTitle("Monitor");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        createUserLabel();

        //creates the Threads for employee and pomodoro
        EmployeeThread employeeThread = new EmployeeThread();
        Thread eThread = new Thread(employeeThread);
        ProgressThread pomodoroThread = new ProgressThread();
        Thread pThread = new Thread(pomodoroThread);

        eThread.start();
        pThread.start();

        //Observes the status of the employees if there is a change than it will call the method
        //for changing the monitor
        DataHub.ipProperty().addListener((observableValue, number, t1) -> {
            employees = DBHandler.getAllEmployee();
            createUserLabel();
        });

        //Observes the pomodoro if there is a change than it will call the method
        //for changing the monitor
        DataHub.pomodoroIpProperty().addListener((observableValue, number, t1) -> {
            pomodoros =DBHandler.getPomodoro();
            createUserLabel();
        });


        stage.setOnCloseRequest(windowEvent -> {
            eThread.stop();
            pThread.stop();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            eThread.stop();
            pThread.stop();
        }));
    }

    /**
     * Creates the main design and label for user, progressbar label and progress arrow
     */
    private void createUserLabel(){
        int count = 0;
        pane.getChildren().clear();
        for (Employee employee : getEmployees()){
            if(department.getValue() != null) {
                if (employee.getStatus() != 0 && department.getSelectionModel().getSelectedItem().equalsIgnoreCase(employee.getDepartment())){
                    userLabel(employee, count);
                    pomodoroLabel(employee, count);
                    progressArrowLabel(employee, count);
                    count++;
                }
            }
        }

        labelLine = new Label();
        labelLine.setPrefWidth(scene.getWindow().getWidth());
        labelLine.setMinHeight(1);
        labelLine.setPrefHeight(1);
        labelLine.setLayoutY(100);
        labelLine.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 1.0), new CornerRadii(5.0), new Insets(-2.5))));
        labelLine.toFront();

        headline = new Label("Pomodoro   Monitor");
        headline.setPrefWidth(scene.getWindow().getWidth());
        headline.setId("headline");
        headline.setPrefHeight(100);
        headline.setAlignment(Pos.CENTER);
        headline.toBack();

        pane.getChildren().addAll(labelLine,headline,departmentLabel,department);
    }

    /**
     * creates the user labels for the consultants where the status is 1 = logged in
     * @param employee is the employee where the status is 1 based on the employeeID
     * @param count is for positioning of the labels
     */
    public void userLabel(Employee employee, int count){
        ImageView myImageView = new ImageView("AppStyle/userImage.png");
        myImageView.setFitHeight(40);
        myImageView.setFitWidth(40);
        user = new Label(employee.getName(),myImageView);
        user.setPrefWidth(125);
        user.setId("user");
        user.setLayoutX(USER_LABEL_X_COORDINATE);
        user.setLayoutY(START_COORDINATE+(Y_COORDINATE*(AVOID_ZERO_MULTIPLIER+count)));
        pane.getChildren().add(user);
    }

    /**
     * creates the progressbar for the different pomodoro
     * @param employee is the employee where the status is 1 and has started a pomodoro based on the employeeID
     * @param count is for positioning of the labels
     */
    public void pomodoroLabel(Employee employee, int count){
        MyProgressbar pomodoro;
        if(DBHandler.createProgressbar(employee.getEmployeeID()) != null){

            pomodoro = DBHandler.createProgressbar(employee.getEmployeeID());
            Project project = DBHandler.getTaskStatus(employee.getEmployeeID());
            int numberOfPomodoro = project.getEstimateTime()/pomodoro.getWorkTime();
            if(project.getStatus().equalsIgnoreCase("in progress") && !project.equals(null)){
                if(numberOfPomodoro > pomodoro.getAmountOfPomodoro()){

                    for (int i = 0; i < numberOfPomodoro; i++) {
                        int prefWorkWidth = TIME_MULTIPLIER * pomodoro.getWorkTime();
                        int prefBreakWidth = TIME_MULTIPLIER * pomodoro.getBreakTime();

                        progressLabel = new Label(pomodoro.getWorkTime()+" min");
                        progressLabel.setId("progressLabel");
                        progressLabel.setPrefWidth(prefWorkWidth);
                        progressLabel.setPrefHeight(PROGRESSBAR_HEIGHT);
                        if(i == 0){
                            progressLabel.setLayoutX(PROGRESS_LABEL_X_COORDINATE + (prefWorkWidth*i));
                        }else {
                            progressLabel.setLayoutX(PROGRESS_LABEL_X_COORDINATE +
                                    (prefWorkWidth*i)+(prefBreakWidth*i));
                        }
                        progressLabel.setLayoutY(START_COORDINATE+(Y_COORDINATE*(AVOID_ZERO_MULTIPLIER+count)));
                        pane.getChildren().add(progressLabel);

                        breakLabel = new Label(pomodoro.getBreakTime()+" min");
                        breakLabel.setId("breakLabel");
                        breakLabel.setPrefWidth(prefBreakWidth);
                        breakLabel.setPrefHeight(PROGRESSBAR_HEIGHT);
                        breakLabel.setLayoutX(PROGRESS_LABEL_X_COORDINATE +
                                (prefWorkWidth*(i+1))+(prefBreakWidth*(i)));
                        breakLabel.setLayoutY(START_COORDINATE+(Y_COORDINATE*(AVOID_ZERO_MULTIPLIER+count)));
                        pane.getChildren().add(breakLabel);
                    }
                }
                if(pomodoro.getAmountOfPomodoro() >= numberOfPomodoro){
                    for (int i = 0; i < pomodoro.getAmountOfPomodoro()+1; i++) {
                        int prefWorkWidth = TIME_MULTIPLIER * pomodoro.getWorkTime();
                        int prefBreakWidth = TIME_MULTIPLIER * pomodoro.getBreakTime();
                        progressLabel = new Label(pomodoro.getWorkTime()+" min");
                        progressLabel.setId("progressLabel");
                        progressLabel.setAlignment(Pos.CENTER);
                        progressLabel.setPrefWidth(prefWorkWidth);
                        progressLabel.setPrefHeight(PROGRESSBAR_HEIGHT);
                        if (i == 0) {
                            progressLabel.setLayoutX(PROGRESS_LABEL_X_COORDINATE + (prefWorkWidth * i));
                        } else {
                            progressLabel.setLayoutX(PROGRESS_LABEL_X_COORDINATE +
                                    (prefWorkWidth * i) + (prefBreakWidth * i));
                        }
                        progressLabel.setLayoutY(START_COORDINATE + (Y_COORDINATE * (AVOID_ZERO_MULTIPLIER + count)));
                        pane.getChildren().add(progressLabel);


                        breakLabel = new Label(pomodoro.getBreakTime()+" min");
                        breakLabel.setId("breakLabel");
                        breakLabel.setAlignment(Pos.CENTER);
                        breakLabel.setPrefWidth(prefBreakWidth);
                        breakLabel.setPrefHeight(PROGRESSBAR_HEIGHT);
                        breakLabel.setLayoutX(PROGRESS_LABEL_X_COORDINATE +
                                (prefWorkWidth * (i + 1)) + (prefBreakWidth * (i)));
                        breakLabel.setLayoutY(START_COORDINATE + (Y_COORDINATE * (AVOID_ZERO_MULTIPLIER + count)));
                        pane.getChildren().add(breakLabel);
                    }
                }
            }else{
               createInfoLabel(count);
            }
        }else{
            createInfoLabel(count);
        }
    }

    /**
     * creates the arrow to show the current progress
     * @param employee gets the currentTime based on the employeeID
     * @param count is for positioning of the labels
     */
    public void progressArrowLabel(Employee employee, int count){

        MyProgressbar pomodoro;
        if(DBHandler.createProgressbar(employee.getEmployeeID()) != null) {
            pomodoro = DBHandler.createProgressbar(employee.getEmployeeID());
            Project project = DBHandler.getTaskStatus(employee.getEmployeeID());
            int numberOfPomodoro = project.getEstimateTime()/pomodoro.getWorkTime();
            if(project.getStatus().equalsIgnoreCase("in progress")){
                double currentTime = DBHandler.getCurrentTime(employee.getEmployeeID());
                assert pomodoro != null;
                if(numberOfPomodoro > 0) {
                    ImageView arrow = new ImageView("AppStyle/Arrow.png");
                    arrow.setFitHeight(20);
                    arrow.setFitWidth(20);

                    if(currentTime < 1 ){
                        double currentWork = Math.round(pomodoro.getWorkTime()*currentTime);
                        arrowLabel = new Label(""+currentWork+" min", arrow);
                        arrowLabel.setLayoutX((((pomodoro.getAmountOfPomodoro())*(TIME_MULTIPLIER*
                                (pomodoro.getBreakTime())))+((pomodoro.getAmountOfPomodoro())*(TIME_MULTIPLIER*
                                (pomodoro.getWorkTime()))))+(PROGRESS_LABEL_X_COORDINATE-10+(TIME_MULTIPLIER*
                                (currentTime * pomodoro.getWorkTime()))));

                        arrowLabel.setLayoutY(START_COORDINATE-GABE_VARIABLE+(Y_COORDINATE*(AVOID_ZERO_MULTIPLIER+count)));
                        pane.getChildren().add(arrowLabel);
                    }
                    else
                    {
                        double currentBreak = Math.round(pomodoro.getBreakTime()*(currentTime-1));
                        arrowLabel = new Label(""+currentBreak+" min", arrow);
                        arrowLabel.setLayoutX(((((pomodoro.getAmountOfPomodoro()-1)*(TIME_MULTIPLIER*
                                (pomodoro.getBreakTime())))+(pomodoro.getAmountOfPomodoro()*(TIME_MULTIPLIER*
                                (pomodoro.getWorkTime()))))+PROGRESS_LABEL_X_COORDINATE-10+(TIME_MULTIPLIER*
                                ((currentTime-1) * pomodoro.getBreakTime()))));

                        arrowLabel.setLayoutY(START_COORDINATE-GABE_VARIABLE+(Y_COORDINATE*(AVOID_ZERO_MULTIPLIER+count)));
                        pane.getChildren().add(arrowLabel);
                    }
                }
            }
         }
     }

    /**
     * creates a label if the employee is logged in but not working
     * @param count is for positioning of the labels
     */
    public void createInfoLabel(int count){
        progressLabel = new Label("NOT WORKING RIGHT NOW");
        progressLabel.setId("notWorkingLabel");
        progressLabel.setPrefHeight(PROGRESSBAR_HEIGHT);
        progressLabel.setLayoutX(PROGRESS_LABEL_X_COORDINATE);
        progressLabel.setLayoutY(START_COORDINATE+(Y_COORDINATE*(1+count)));
        pane.getChildren().add(progressLabel);
    }

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static ArrayList<Pomodoro> getPomodoros() {
        return pomodoros;
    }

}
