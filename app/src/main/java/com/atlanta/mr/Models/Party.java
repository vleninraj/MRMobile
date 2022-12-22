package com.atlanta.mr.Models;

public class Party {

    private int _id;
    private String _PartyCode;
    private String _PartyName;
    private String _ArabicName;
    private String _GSTNO;
    private String _VATNO;
    private Double _Balance;
    private String _MobileNumber;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_PartyCode() {
        return _PartyCode;
    }
    public String toString() {
        return _PartyName;
    }

    public void set_PartyCode(String _PartyCode) {
        this._PartyCode = _PartyCode;
    }

    public String get_PartyName() {
        return _PartyName;
    }

    public void set_PartyName(String _PartyName) {
        this._PartyName = _PartyName;
    }

    public String get_ArabicName() {
        return _ArabicName;
    }

    public void set_ArabicName(String _ArabicName) {
        this._ArabicName = _ArabicName;
    }

    public String get_GSTNO() {
        return _GSTNO;
    }

    public void set_GSTNO(String _GSTNO) {
        this._GSTNO = _GSTNO;
    }

    public String get_VATNO() {
        return _VATNO;
    }

    public void set_VATNO(String _VATNO) {
        this._VATNO = _VATNO;
    }

    public Double get_Balance() {
        return _Balance;
    }

    public void set_Balance(Double _Balance) {
        this._Balance = _Balance;
    }

    public String get_MobileNumber() {
        return _MobileNumber;
    }

    public void set_MobileNumber(String _MobileNumber) {
        this._MobileNumber = _MobileNumber;
    }
}

