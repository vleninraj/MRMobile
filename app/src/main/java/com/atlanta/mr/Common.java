package com.atlanta.mr;

import com.atlanta.mr.Models.LoginUser;
import com.atlanta.mr.Models.MRDTL;

import java.util.ArrayList;

public class Common {

    public static String DomainName="atACCWeb";
    public  static int CurrentBranchID=0;
    public  static  int CurrentDepoID=0;
    public  static  int iFiscalID=0;
    public  static  int iPurchaseTypeID=0;
    public  static  int iPartyID=0;
    public static  String sPartyName;
    public static String sPurchaseType;


    //Company Details
    public static String CompanyName="";
    public static String Address1="";
    public static String Address2="";
    public static String Address3="";
    public static int NoofDecimals=2;
    public static int NoofDecimalsQty=1;
    public static String CurrencySymbol="";
    public static String Country="";
    public static String sDecimals="";
    public static String sDecimalsQty="";
    public static String sipAddress;
    public static LoginUser currentUser;
    public static ArrayList<MRDTL> _mrdtls;



}
