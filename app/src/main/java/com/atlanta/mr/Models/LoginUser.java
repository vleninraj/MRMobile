package com.atlanta.mr.Models;

public class LoginUser {
    private  int id;
    private String Name;
    private  int EmployeeID;
   private  String Password;
   private String PinNumber;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPinNumber() {
        return PinNumber;
    }

    public void setPinNumber(String pinNumber) {
        PinNumber = pinNumber;
    }
}
