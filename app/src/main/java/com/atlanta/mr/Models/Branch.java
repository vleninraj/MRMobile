package com.atlanta.mr.Models;

public class Branch {
    private int _id;
    private String _Code;
    private String _Name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
    @Override
    public String toString() {
        return _Name;
    }
    public String get_Code() {
        return _Code;
    }

    public void set_Code(String _Code) {
        this._Code = _Code;
    }

    public String get_Name() {
        return _Name;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }
}
