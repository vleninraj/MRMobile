package com.atlanta.mr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.mr.Models.Branch;
import com.atlanta.mr.Models.Depo;
import com.atlanta.mr.Models.Fiscal;
import com.atlanta.mr.Models.Party;
import com.atlanta.mr.Models.PurchaseType;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {


    EditText txtipaddress;
    AutoCompleteTextView txtBranch;
    AutoCompleteTextView txtDepo;
    AutoCompleteTextView txtfinancialPeriod;
    AutoCompleteTextView txtpurchasetype;
    AutoCompleteTextView txtdefaultparty;
    Button btnsavesettings;
    RequestQueue requestQueue;
    String sIpAddress="";
    final ArrayList<Branch> _branches=new ArrayList<>();
    final ArrayList<Depo> _depos=new ArrayList<>();
    final ArrayList<Fiscal> _fiscals=new ArrayList<>();
    final ArrayList<PurchaseType> _purchasetypes=new ArrayList<>();
    ArrayList<Party> _parties=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        txtipaddress =findViewById(R.id.txtipaddress);
        txtBranch=findViewById(R.id.txtbranch);
        txtDepo=findViewById(R.id.txtdepo);
        txtfinancialPeriod=findViewById(R.id.txtfinancialPeriod);
        txtpurchasetype=findViewById(R.id.txtpurchasetype);
        txtdefaultparty=findViewById(R.id.txtdefaultparty);
        btnsavesettings = findViewById(R.id.btnsavesettings);
        requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences ipAddress = getApplicationContext().getSharedPreferences("ipaddress", MODE_PRIVATE);
        sIpAddress=ipAddress.getString("ipaddress", "");
        txtipaddress.setText(sIpAddress);
        int iBranchID=ipAddress.getInt("BranchID",0);
        int iDepoID=ipAddress.getInt("DepoID",0);
        int iFiscalID=ipAddress.getInt("FiscalID",0);
        int iPurchaseTypeID=ipAddress.getInt("PurchaseTypeID",0);
        int iPartyID=ipAddress.getInt("PartyID",0);

        LoadBranches();
        LoadFinancialPeriod();
        LoadPurchaseTypes();
        LoadParties();
        if(iBranchID!=0) {
            GetBranchByID(iBranchID);
        }
        if(iDepoID!=0) {
            GetDepoByID(iDepoID);
        }
        if(iFiscalID!=0)
        {
            GetFiscalByID(iFiscalID);
        }
        if(iPurchaseTypeID!=0)
        {
            GetPurchaseTypeByID(iPurchaseTypeID);
        }
        if(iPartyID!=0)
        {
            GetPartyNameByID(iPartyID);
        }

        txtBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if(item instanceof Branch )
                {
                    int iBranchID=((Branch)item).get_id();
                    txtBranch.setTag(iBranchID);
                    LoadDepos(iBranchID);
                }

            }
        });
        txtDepo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if(item instanceof Depo )
                {
                    int iDepoID=((Depo)item).get_id();
                    txtDepo.setTag(iDepoID);
                }
            }
        });
        txtfinancialPeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if(item instanceof Fiscal )
                {
                    int ifiscalid=((Fiscal)item).getId();
                    txtfinancialPeriod.setTag(ifiscalid);
                }
            }
        });
        txtpurchasetype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if(item instanceof PurchaseType)
                {
                    int id=((PurchaseType)item).getId();
                    txtpurchasetype.setTag(id);
                }
            }
        });
        txtdefaultparty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if(item instanceof Party )
                {
                    int id=((Party)item).get_id();
                    txtdefaultparty.setTag(id);
                }
            }
        });


        btnsavesettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtipaddress.getText().toString().trim().equals(""))
                {
                    Toast.makeText(SettingsActivity.this,"IP can't be blank!",Toast.LENGTH_LONG).show();
                    return;
                }
                int iBranchID=0;
                int iDepoID=0;
                int iFiscalID=0;
                int iPurchaseTypeID=0;
                int iPartyID=0;
                if(txtBranch.getTag()!=null)
                {
                    iBranchID=Integer.valueOf(txtBranch.getTag().toString());
                }
                if(txtDepo.getTag()!=null)
                {
                    iDepoID=Integer.valueOf(txtDepo.getTag().toString());
                }
                if(txtfinancialPeriod.getTag()!=null)
                {
                    iFiscalID=Integer.valueOf(txtfinancialPeriod.getTag().toString());
                }
                if(txtpurchasetype.getTag()!=null)
                {
                    iPurchaseTypeID=Integer.valueOf(txtpurchasetype.getTag().toString());
                }
                if(txtdefaultparty.getTag()!=null)
                {
                    iPartyID=Integer.valueOf(txtdefaultparty.getTag().toString());
                }

                SharedPreferences.Editor ipAddressEditor = ipAddress.edit();
                ipAddressEditor.putString("ipaddress", txtipaddress.getText().toString().trim());
                ipAddressEditor.putInt("BranchID",iBranchID);
                ipAddressEditor.putInt("DepoID",iDepoID);
                ipAddressEditor.putInt("FiscalID",iFiscalID);
                ipAddressEditor.putInt("PurchaseTypeID",iPurchaseTypeID);
                ipAddressEditor.putInt("PartyID",iPartyID);
                ipAddressEditor.putString("PartyName",txtdefaultparty.getText().toString());
                ipAddressEditor.putString("PurchaseType",txtpurchasetype.getText().toString());
                ipAddressEditor.apply();
                Toast.makeText(SettingsActivity.this,"Settings Saved...",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private  void LoadParties()
    {
        ArrayList<Party> _parties=new ArrayList<>();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Party";
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _parties.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Party _party=new Party();
                        _party.set_id(jsonObject.getInt("id"));
                        _party.set_PartyCode(jsonObject.getString("PartyCode"));
                        _party.set_ArabicName(jsonObject.getString("ArabicName"));
                        _party.set_PartyName(jsonObject.getString("PartyName"));
                        _party.set_Balance(jsonObject.getDouble("Balance"));
                        _party.set_MobileNumber(jsonObject.getString("MobileNumber"));
                        _parties.add(_party);
                    }
                    Party[] itemsArray = _parties.toArray(new Party[_parties.size()]);
                    ArrayAdapter<Party> adapter = new ArrayAdapter<Party>(SettingsActivity.this, android.R.layout.simple_list_item_1, itemsArray);
                    txtdefaultparty.setAdapter(adapter);
                    txtdefaultparty.setThreshold(0);

                }
                catch (Exception ex)
                {
                    Toast.makeText(SettingsActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void LoadFinancialPeriod()
    {
        if(sIpAddress.equals(""))
        {
            return;
        }
        _fiscals.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Fiscal";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _fiscals.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Fiscal _fiscal=new Fiscal();
                        _fiscal.setId(jsonObject.getInt("id"));
                        _fiscal.setName(jsonObject.getString("Name"));

                        _fiscals.add(_fiscal);
                    }
                    Fiscal[] itemsArray = _fiscals.toArray(new Fiscal[_fiscals.size()]);
                    ArrayAdapter<Fiscal> adapter = new ArrayAdapter<Fiscal>(SettingsActivity.this, android.R.layout.simple_list_item_1, itemsArray);
                    txtfinancialPeriod.setAdapter(adapter);
                    txtfinancialPeriod.setThreshold(0);

                }
                catch (Exception w)
                {
                    Toast.makeText(SettingsActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void LoadPurchaseTypes()
    {
        if(sIpAddress.equals(""))
        {
            return;
        }
        _purchasetypes.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/getpurchasetype";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _purchasetypes.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PurchaseType _purchasetype=new PurchaseType();
                        _purchasetype.setId(jsonObject.getInt("id"));
                        _purchasetype.setName(jsonObject.getString("Name"));
                        _purchasetypes.add(_purchasetype);
                    }
                    PurchaseType[] itemsArray = _purchasetypes.toArray(new PurchaseType[_fiscals.size()]);
                    ArrayAdapter<PurchaseType> adapter = new ArrayAdapter<PurchaseType>(SettingsActivity.this, android.R.layout.simple_list_item_1, itemsArray);
                    txtpurchasetype.setAdapter(adapter);
                    txtpurchasetype.setThreshold(0);

                }
                catch (Exception w)
                {
                    Toast.makeText(SettingsActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void LoadBranches()
    {
        if(sIpAddress.equals(""))
        {
            return;
        }
        _branches.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Branch";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _branches.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Branch _branch=new Branch();
                        _branch.set_id(jsonObject.getInt("id"));
                        _branch.set_Code(jsonObject.getString("Code"));
                        _branch.set_Name(jsonObject.getString("Name"));
                        _branches.add(_branch);
                    }
                    Branch[] itemsArray = _branches.toArray(new Branch[_branches.size()]);
                    ArrayAdapter<Branch> adapter = new ArrayAdapter<Branch>(SettingsActivity.this, android.R.layout.simple_list_item_1, itemsArray);
                    txtBranch.setAdapter(adapter);
                    txtBranch.setThreshold(0);

                }
                catch (Exception w)
                {
                    Toast.makeText(SettingsActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void LoadDepos(int BranchID)
    {
        if(sIpAddress.equals(""))
        {
            return;
        }
        _depos.clear();
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Depo?BranchID=" + BranchID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _depos.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Depo _depo=new Depo();
                        _depo.set_id(jsonObject.getInt("id"));
                        _depo.set_Code(jsonObject.getString("Code"));
                        _depo.set_Name(jsonObject.getString("Name"));
                        _depos.add(_depo);
                    }
                    Depo[] itemsArray = _depos.toArray(new Depo[_depos.size()]);
                    ArrayAdapter<Depo> adapter = new ArrayAdapter<Depo>(SettingsActivity.this, android.R.layout.simple_list_item_1, itemsArray);
                    txtDepo.setAdapter(adapter);
                    txtDepo.setThreshold(0);

                }
                catch (Exception w)
                {
                    Toast.makeText(SettingsActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void GetBranchByID(int id)
    {
        if(sIpAddress.equals(""))
        {
            return;
        }

        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Branch?id=" + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    if(jsonArray.length()>0)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Branch _branch=new Branch();
                        _branch.set_id(jsonObject.getInt("id"));
                        _branch.set_Code(jsonObject.getString("Code"));
                        _branch.set_Name(jsonObject.getString("Name"));
                        txtBranch.setText(_branch.get_Name());
                        txtBranch.setTag(_branch.get_id());
                    }


                }
                catch (Exception w)
                {
                    Toast.makeText(SettingsActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void GetDepoByID(int id)
    {
        if(sIpAddress.equals(""))
        {
            return;
        }

        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Depo?id=" + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    if(jsonArray.length()>0)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Depo _depo=new Depo();
                        _depo.set_id(jsonObject.getInt("id"));
                        _depo.set_Code(jsonObject.getString("Code"));
                        _depo.set_Name(jsonObject.getString("Name"));
                        txtDepo.setText(_depo.get_Name());
                        txtDepo.setTag(_depo.get_id());
                    }


                }
                catch (Exception w)
                {
                    Toast.makeText(SettingsActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private  void GetPurchaseTypeByID(int id)
    {
        if(sIpAddress.equals(""))
        {
            return;
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/getmastervalue?id=" + id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response!=null && !response.equals(""))
                {
                    String sResult=response.replace("\"", "");
                    txtpurchasetype.setText(sResult);
                    txtpurchasetype.setTag(id);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    private  void GetPartyNameByID(int id)
    {
        if(sIpAddress.equals(""))
        {
            return;
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/getAccountNameByID?id=" + id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response!=null && !response.equals(""))
                {
                    String sResult=response.replace("\"", "");
                    txtdefaultparty.setText(sResult);
                    txtdefaultparty.setTag(id);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    private  void GetFiscalByID(int id)
    {
        if(sIpAddress.equals(""))
        {
            return;
        }

        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/Fiscal?id=" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response!=null) {
                        String sfiscalname = response.getString("Name");
                        int fiscalid=response.getInt("id");
                        txtfinancialPeriod.setText(sfiscalname);
                        txtfinancialPeriod.setTag(fiscalid);

                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(SettingsActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}