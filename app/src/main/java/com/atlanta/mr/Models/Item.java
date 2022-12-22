package com.atlanta.mr.Models;

public class Item {

    private  int id;
    private String Code;
    private String Name;

    public int getId() {
        return id;
    }
    @Override
    public String toString() {
        return Name;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
