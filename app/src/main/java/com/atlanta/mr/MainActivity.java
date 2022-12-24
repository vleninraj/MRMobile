package com.atlanta.mr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.mr.Models.LoginUser;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnlogin;
    Button btnSettings;
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
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSettings=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intSettings);
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