package com.atlanta.mr.Models;

public class Item {

    private  int id;
    private String Code;
    private String Name;
    private String Unit;
    private Double PurchaseRate;
    private int UnitID;

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

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }



    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public Double getPurchaseRate() {
        return PurchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        PurchaseRate = purchaseRate;
    }
}
