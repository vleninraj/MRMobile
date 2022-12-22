package com.atlanta.mr.Models;

public class Unit {
    private int id;
    private String Name;
    private Double Cf;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getCf() {
        return Cf;
    }

    public void setCf(Double cf) {
        Cf = cf;
    }
}
