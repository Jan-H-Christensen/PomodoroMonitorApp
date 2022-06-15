package DataHandlers;

import DatabaseObjects.Employee;
import AppStyle.MyProgressbar;
import DatabaseObjects.Pomodoro;
import DatabaseObjects.Project;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * this class is for interacting with the database of the company, we are only reading from the database
 * because we are showing the current status / progress of the consultants based on the different departments
 *
 * @author Jan Christensen
 * @version 1.0
 * @since 06.06.2022
 */

public class DBHandler {

    private static Connection con;

    /**
     * sets up the connection to the database
     */
    private static void connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1544;databaseName=JensenAndJensen", "sa", "1234");

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * a list of all Employees in the company to check the current status
     * @return the current consultants
     */
    public static ArrayList<Employee> getAllEmployee() {
        connect();
        try {
            PreparedStatement ps = con.prepareStatement("exec getEmployee");
            ResultSet rs = ps.executeQuery();

            ArrayList<Employee> employees = new ArrayList<>();
            while (rs.next()){

                int employeeID = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                int phone = rs.getInt(4);
                String address = rs.getString(5);
                int status = rs.getInt(6);
                String department = rs.getString(7);

                Employee employee = new Employee(employeeID,name,email,phone,address,status,department);
                employees.add(employee);

            }
            return employees;
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }finally {
            disconnect();
        }
    }

    /**
     * closing the connection to the database
     */
    private static void disconnect() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * a list of all current existing pomodoro is needed for the calculation of the progressbar
     * @return a list of all the pomodoro
     */
    public static ArrayList<Pomodoro> getPomodoro(){
        connect();
        try{
            int check = 0;
            PreparedStatement ps = con.prepareStatement("exec getPomodoro ");
            ResultSet rs = ps.executeQuery();
            ArrayList<Pomodoro> pomodoroList = new ArrayList<>();
            while (rs.next()){
                int pomodoroID = rs.getInt(1);
                int workTime = rs.getInt(2);
                int breakTime = rs.getInt(3);

                Pomodoro pomodoro = new Pomodoro(pomodoroID,workTime,breakTime);
                pomodoroList.add(pomodoro);
                check++;
            }
            if(check > 0){
                return pomodoroList;
            }else {
                return null;
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }finally {
            disconnect();
        }
    }

    /**
     * gets the needed variables for creating the progressbar
     * @param employeeID is the employeeID of the consultants where the status is 1
     * @return a MyProgressbar object
     */
    public static MyProgressbar createProgressbar(int employeeID){
        connect();
        Date date = new java.util.Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");

        try{
            int check = 0;
            MyProgressbar progressbar = null;
            PreparedStatement ps = con.prepareStatement("exec createProgressbar "+employeeID+",'"+dateFormat.format(date)+"'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int amountOfPomodoro = rs.getInt(1);
                int workTime = rs.getInt(2);
                int breakTime = rs.getInt(3);

                progressbar = new MyProgressbar(amountOfPomodoro,workTime,breakTime);
                check++;
            }
            if(check > 0){
                return progressbar;
            }else {
                return null;
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }finally {
            disconnect();
        }
    }

    /**
     * gets the current time of the current pomodoro
     * @param employeeID is needed to find the current time for every consultant
     * @return the current time
     */
    public static double getCurrentTime(int employeeID){
        connect();
        try{
            int check =0;
            double time = 0;
            PreparedStatement ps = con.prepareStatement("exec getCurrentTime "+employeeID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                 double currentTime = rs.getDouble(1);
                 time = currentTime;
                 check++;
            }
            if (check != 0){
                return time;
            }
            else{
                return 0;
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return 0;
        }finally {
            disconnect();
        }
    }

    /**
     * gets the task / pomodoro status for at initialize / draw progressbar
     * @param employeeID is needed to find out if consultant has started pomodoro
     * @return a Project object
     */
    public static Project getTaskStatus(int employeeID){
        connect();
        Date date = new java.util.Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        try{
            int check = 0;
            Project project = null;
            PreparedStatement ps = con.prepareStatement("exec getTask "+employeeID+",'"+dateFormat.format(date)+"'");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int taskID = rs.getInt(1);
                int estimateTime = rs.getInt(2);
                int numberOfWorkOn = rs.getInt(3);
                String taskName = rs.getString(4);
                String status = rs.getString(5);

                project = new Project(taskID,estimateTime,numberOfWorkOn,taskName,status);
                check++;
            }
            if(check != 0){
                return project;
            }else{
                return null;
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }finally {
            disconnect();
        }
    }
}