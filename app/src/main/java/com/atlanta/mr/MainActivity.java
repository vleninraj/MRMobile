package com.atlanta.mr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.mr.Models.LoginUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnlogin;
    Button btnSettings;
    Button btnregister;
    EditText txtpinnumber;
    String sIpAddress;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnlogin=findViewById(R.id.btnlogin);
        btnSettings=findViewById(R.id.btnopensettings);
        txtpinnumber=findViewById(R.id.txtpinnumber);
        btnregister=findViewById(R.id.btnregister);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        Common.CurrentBranchID=ipAddress.getInt("BranchID",0);
        Common.CurrentDepoID =ipAddress.getInt("DepoID",0);
        Common.iFiscalID=ipAddress.getInt("FiscalID",0);
        Common.iPurchaseTypeID=ipAddress.getInt("PurchaseTypeID",0);
        Common.iPartyID=ipAddress.getInt("PartyID",0);
        Common.sPartyName=ipAddress.getString("PartyName", "");
        Common.sPurchaseType=ipAddress.getString("PurchaseType", "");
        Common.sipAddress=sIpAddress;
        requestQueue = Volley.newRequestQueue(this);
        CheckRegistration(false);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSettings=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intSettings);
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckRegistration(true);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.currentUser=null;
                if(Common.iPurchaseTypeID==0)
                {
                    Toast.makeText(getApplicationContext(),"Must set default purchase type before login!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtpinnumber.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"PIN Number must be entered!",Toast.LENGTH_LONG).show();
                    txtpinnumber.requestFocus();
                    return;
                }
                if(sIpAddress.equals(""))
                {
                    Toast.makeText(MainActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
                    return;
                }
                Common.currentUser=new LoginUser();
                String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/LoginUser?PinNumber=" + txtpinnumber.getText().toString();
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response!=null)
                            {
                                Common.currentUser=new LoginUser();
                                Common.currentUser.setName(response.getString("UserName"));
                                Common.currentUser.setId(response.getInt("id"));
                                Common.currentUser.setEmployeeID(response.getInt("SalesManID"));
                                Common.currentUser.setPassword(response.getString("Password"));
                                Common.currentUser.setPinNumber(response.getString("PINNumber"));
                                Toast.makeText(getApplicationContext(),"Login Success...",Toast.LENGTH_LONG).show();
                                LoadCompanyDetails();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception w)
                        {
                            Toast.makeText(getApplicationContext(),w.getMessage(),Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);

            }
        });



    }
    @Override
    public void onResume(){
        super.onResume();

        btnregister.setEnabled(true);
        btnregister.setTextColor(Color.WHITE);
    }
    public void CheckRegistration(boolean blnFromButton)
    {
        btnlogin.setEnabled(false);
        btnlogin.setTextColor(Color.GRAY);
        btnregister.setEnabled(false);
        btnregister.setTextColor(Color.GRAY);

        String sProductID= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences regpref = getApplicationContext().getSharedPreferences("regno", MODE_PRIVATE);
       String sRegNo=regpref.getString("regno", "");
        getRegistrationNo(sProductID,sRegNo,blnFromButton);

    }
    public  void getRegistrationNo(String sProductID,String sRegNo,boolean blnFromButton)
    {

        String url="https://atlanta-it.com/RMS/getregno.php?productid=" + sProductID + "&key=112";
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response!=null) {
                    try {
                        JSONObject _jsnresponse=response.getJSONObject(0);
                        String sActualRegNo=_jsnresponse.getString("regno");

                        if(!blnFromButton && sRegNo.equals(sActualRegNo))
                        {
                            if(blnFromButton)
                            {
                                Toast.makeText(getApplicationContext(),"Already Registered!",Toast.LENGTH_LONG).show();
                            }
                            btnlogin.setEnabled(true);
                            btnlogin.setTextColor(Color.WHITE);
                            btnregister.setVisibility(View.GONE);

                            return ;
                        }
                        LayoutInflater inflater=getLayoutInflater();
                        final View DialogView=inflater.inflate(R.layout.activity_registration,null);
                        final EditText txtproductid=DialogView.findViewById(R.id.txtproductid);
                        final EditText txtregistrationno=DialogView.findViewById(R.id.txtregistrationno);
                        final Button btnregisterok=DialogView.findViewById(R.id.btnregister);
                        final Button btnclose=DialogView.findViewById(R.id.btnclose);
                        AlertDialog alert=new AlertDialog.Builder(MainActivity.this).create();
                        txtproductid.setText(sProductID);
                        txtregistrationno.requestFocus();
                        btnregisterok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(txtregistrationno.getText().toString().trim().equals(""))
                                {
                                    txtregistrationno.setError("Enter Registration No!");
                                    txtregistrationno.requestFocus();
                                    return ;
                                }
                                String sCurrentRegNo=txtregistrationno.getText().toString();
                                if(sCurrentRegNo.trim().equals(sActualRegNo.trim()))
                                {
                                    final SharedPreferences regpref = getApplicationContext().getSharedPreferences("regno", MODE_PRIVATE);
                                    SharedPreferences.Editor ipAddressEditor = regpref.edit();
                                    ipAddressEditor.putString("regno", sActualRegNo);
                                    ipAddressEditor.apply();
                                    Toast.makeText(MainActivity.this,"Registration Success...",Toast.LENGTH_LONG).show();
                                    btnlogin.setEnabled(true);
                                    btnlogin.setTextColor(Color.WHITE);
                                    btnregister.setVisibility(View.GONE);
                                    alert.dismiss();
                                }
                                else
                                {

                                    Toast.makeText(getApplicationContext(),"Invalid Registration No!",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });
                        btnclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                btnregister.setEnabled(true);
                                btnregister.setTextColor(Color.WHITE);
                                alert.dismiss();
                            }
                        });

                        alert.setView(DialogView);
                        alert.show();

                    }
                    catch (JSONException e)
                    {
                        btnregister.setEnabled(true);
                        btnregister.setTextColor(Color.WHITE);
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnregister.setEnabled(true);
                btnregister.setTextColor(Color.WHITE);
            }
        });
        requestQueue.add(request);

    }
    public void LoadCompanyDetails()
    {
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        Common.sipAddress=sIpAddress;
        if(sIpAddress.equals(""))
        {
            Toast.makeText(MainActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
            return;
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Company";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response!=null) {
                        Common.CompanyName = response.getString("CompanyName");
                        Common.Address1 = response.getString("Address1");
                        Common.Address2 = response.getString("Address2");
                        Common.Address3 = response.getString("Address3");
                        Common.CurrencySymbol = response.getString("CurrencySymbol");
                        Common.Country = response.getString("Country");
                        Common.NoofDecimals = response.getInt("NoofDecimals");
                        Common.NoofDecimalsQty=response.getInt("NoofDecimalinQty");
                        Common.sDecimals="%." + Common.NoofDecimals + "f";
                        Common.sDecimalsQty="%." + Common.NoofDecimalsQty + "f";
                        Intent intent = new Intent(MainActivity.this, PosListActivity.class);
                        startActivity(intent);
                    }
                }
                catch (Exception w)
                {
                    Toast.makeText(MainActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}