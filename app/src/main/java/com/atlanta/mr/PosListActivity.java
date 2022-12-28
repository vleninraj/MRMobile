package com.atlanta.mr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.mr.Adapter.MRListAdapter;
import com.atlanta.mr.Models.Depo;
import com.atlanta.mr.Models.Fiscal;
import com.atlanta.mr.Models.MRList;
import com.atlanta.mr.Models.Party;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;

public class PosListActivity extends AppCompatActivity {

    Button btnNewOrder;
    GridView grdpos;
    String sIpAddress;
    RequestQueue requestQueue;
    SearchView searchvw;
    Button btnsearch;
    final ArrayList<MRList> _vouchers=new ArrayList<>();
    final ArrayList<MRList> _vouchersfiltered=new ArrayList<>();
    Boolean blnisAdmin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_list);
        requestQueue = Volley.newRequestQueue(this);
        btnNewOrder=findViewById(R.id.btnneworder);
        grdpos=findViewById(R.id.grdpos);
        searchvw=findViewById(R.id.searchvw);
        btnsearch=findViewById(R.id.btnsearch);
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
        searchvw.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                btnsearch.setVisibility(View.VISIBLE);
                searchvw.setVisibility(View.GONE);
                return true;
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsearch.setVisibility(View.GONE);
                searchvw.setVisibility(View.VISIBLE);
                searchvw.setIconified(false);
            }
        });
        grdpos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            if(!blnisAdmin)
            {
                Toast.makeText(getApplicationContext(),"Only admin or supervisor can edit invoices!",Toast.LENGTH_LONG).show();
                return;
            }
                MRList  _pos= _vouchers.get(i);
                if(_pos!=null)
                {
                    if(_pos.getVoucherNo().equals(""))
                    {
                        Toast.makeText(PosListActivity.this,"Invalid Voucher!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(PosListActivity.this , NewPOSActivity.class);
                    intent.putExtra("NewRecord", false);
                    intent.putExtra("PartyName",Common.sPartyName );
                    intent.putExtra("PartyID", Common.iPartyID);
                    intent.putExtra("HDRID", _pos.getId());
                    startActivity(intent);

                }
            }
        });
        searchvw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                _vouchersfiltered.clear();
                for (MRList _pos : _vouchers) {
                    if (_pos.getVoucherNo().toUpperCase().startsWith(s.toString().toUpperCase())
                            || _pos.getVoucherNo().toUpperCase().endsWith(s.toString().toUpperCase())) {
                        _vouchersfiltered.add(_pos);
                    }
                }
                MRListAdapter adapter = new MRListAdapter(PosListActivity.this, _vouchersfiltered);
                grdpos.setAdapter(adapter);
                return true;
            }
        });
        isAdmin();
        LoadPOS();

        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // if( Common.iPartyID==0) {

                    ShowPartyDialog();
              //  }
              //  else
              //  {
                //    Intent intentNewPOS = new Intent(PosListActivity.this, NewPOSActivity.class);
                //    intentNewPOS.putExtra("NewRecord", true);
                //    intentNewPOS.putExtra("PartyName",Common.sPartyName );
               //     intentNewPOS.putExtra("PartyID", Common.iPartyID);
                //    intentNewPOS.putExtra("HDRID", 0);
                //    startActivity(intentNewPOS);
               // }
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();

        LoadPOS();
    }
    private  void isAdmin()
    {
        if(Common.currentUser==null){ return; }
        String sPassowrd=Common.currentUser.getPassword();
        String url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/isAdmin?password=" + sPassowrd;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(response!=null)
                    {
                        String sResult=response.replace("\"", "");
                       if(sResult.trim()!="")
                       {
                           blnisAdmin=true;
                       }
                       else
                       {
                           blnisAdmin=false;
                       }

                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(PosListActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PosListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }
    private  void ShowPartyDialog()
    {
        LayoutInflater layoutInflater = getLayoutInflater();
        final View dialog = layoutInflater.inflate(R.layout.activity_selectparty, null);
        final AlertDialog alert = new AlertDialog.Builder(PosListActivity.this).create();
        final AutoCompleteTextView txtselectparty = dialog.findViewById(R.id.txtselectparty);
        final Button btnok = dialog.findViewById(R.id.btnok);
        final Button btnCancel = dialog.findViewById(R.id.btncancel);
        txtselectparty.requestFocus();
        ArrayList<Party> _parties = new ArrayList<>();
        String url = "http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/GetSuppliers";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _parties.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Party _party = new Party();
                        _party.set_id(jsonObject.getInt("id"));
                        _party.set_PartyCode(jsonObject.getString("PartyCode"));
                        _party.set_ArabicName(jsonObject.getString("ArabicName"));
                        _party.set_PartyName(jsonObject.getString("PartyName"));
                        _party.set_Balance(jsonObject.getDouble("Balance"));
                        _party.set_MobileNumber(jsonObject.getString("MobileNumber"));
                        _parties.add(_party);
                    }
                    Party[] itemsArray = _parties.toArray(new Party[_parties.size()]);
                    ArrayAdapter<Party> adapter = new ArrayAdapter<Party>(PosListActivity.this, android.R.layout.simple_list_item_1, itemsArray);
                    txtselectparty.setAdapter(adapter);
                    txtselectparty.setThreshold(0);

                } catch (Exception ex) {
                    Toast.makeText(PosListActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PosListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtselectparty.getText().toString().equals("")) {
                    txtselectparty.setError("Party must be selected!");
                    txtselectparty.requestFocus();
                    return;
                }
                alert.dismiss();
                Intent intentNewPOS = new Intent(PosListActivity.this, NewPOSActivity.class);
                intentNewPOS.putExtra("NewRecord", true);
                intentNewPOS.putExtra("PartyName", txtselectparty.getText().toString());
                intentNewPOS.putExtra("PartyID", Integer.valueOf(txtselectparty.getTag().toString()));
                intentNewPOS.putExtra("HDRID", 0);
                startActivity(intentNewPOS);
            }
        });
        txtselectparty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if (item instanceof Party) {
                    int ipartyid = ((Party) item).get_id();
                    txtselectparty.setTag(ipartyid);
                }
            }
        });

        alert.setView(dialog);
        alert.show();
    }
    private void LoadPOS()
    {
        if(sIpAddress.equals(""))
        {
            Toast.makeText(PosListActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
            return;
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/mrlist?PurchaseTypeID=" + Common.iPurchaseTypeID;
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    _vouchers.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MRList pos=new MRList();
                        pos.setVoucherNo(jsonObject.getString("VoucherNo"));
                        pos.setId(jsonObject.getInt("id"));
                        pos.setVoucherDate(jsonObject.getString("VoucherDate"));
                        pos.setGrandTotal(jsonObject.getDouble("GrandTotal"));
                        pos.setParty(jsonObject.getString("Party"));
                        _vouchers.add(pos);
                    }
                    MRListAdapter adapter = new MRListAdapter(PosListActivity.this , _vouchers);
                    grdpos.setAdapter(adapter);
                }
                catch (Exception ex)
                {
                    Toast.makeText(PosListActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PosListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }
}