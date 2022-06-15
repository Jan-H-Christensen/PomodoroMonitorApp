
package DatabaseObjects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
/**
 * creates an Employee object to get the needed variable about the consultants
 *
 * @author Jan Christensen
 * @version 1.0
 * @since 06.06.2022
 */
public class Employee {
    private int employeeID;
    private String name;
    private String email;
    private int phone;
    private String address;
    private int status;
    private String department;


    public Employee(int employeeID, String name, String email, int phone, String address, int status, String department) {
        this.employeeID = employeeID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.department = department;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getStatus() {
        return status;
    }


    public String getDepartment() {
        return department;
    }
}
