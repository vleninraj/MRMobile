package com.atlanta.mr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.mr.Adapter.MRDTLAdapter;
import com.atlanta.mr.Adapter.MRListAdapter;
import com.atlanta.mr.Models.Item;
import com.atlanta.mr.Models.LoginUser;
import com.atlanta.mr.Models.MRDTL;
import com.atlanta.mr.Models.MRList;
import com.atlanta.mr.Models.Unit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.speech.tts.TextToSpeech;

public class NewPOSActivity extends AppCompatActivity {

    String sIpAddress="";
    RequestQueue requestQueue;
    Boolean blnNewRecord=false;
    Calendar cal = Calendar.getInstance();
    TextView tvvoucherno,tvparty,tvloginuser,dtdate,txtbillamount;
    Button btnCalandar,btnsaveinvoice,btnadditem,btnreadbarcode,btnnewbill,btnfavourite;
    EditText txtbarcode;
    GridView grdnewpos;
    String sPartyName;
    int iPartyID,iHdrID;
    EditText txtrate;
    TextView txtamount;
    EditText txtqty;
    MRDTLAdapter _posDtlAdapter;
    Boolean blnSavingStart=false;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_posactivity);
        tvvoucherno=findViewById(R.id.tvvoucherno);
        tvparty=findViewById(R.id.tvparty);
        tvloginuser=findViewById(R.id.tvloginuser);
        dtdate=findViewById(R.id.dtdate);
        txtbillamount=findViewById(R.id.txtbillamount);
        btnCalandar=findViewById(R.id.btnCalandar);
        btnsaveinvoice=findViewById(R.id.btnsaveinvoice);
        btnadditem=findViewById(R.id.btnadditem);
        txtbarcode=findViewById(R.id.txtbarcode);
        grdnewpos=findViewById(R.id.grdnewpos);
        btnnewbill=findViewById(R.id.btnnewbill);
        btnreadbarcode=findViewById(R.id.btnreadbarcode);
        btnfavourite=findViewById(R.id.btnfavourite);

        requestQueue = Volley.newRequestQueue(this);
        final Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        blnNewRecord=bd.getBoolean("NewRecord",false);
        sPartyName=bd.getString("PartyName","");
        iPartyID=bd.getInt("PartyID",0);
        iHdrID=bd.getInt("HDRID",0);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

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

        if(blnNewRecord) {
            ClearAll();
            getVoucherNo();
        }
      else
        {
            GetData(iHdrID);
        }
        txtbarcode.requestFocus();



        btnnewbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClearAll();
                getVoucherNo();

                txtbarcode.requestFocus();
            }
        });
        txtbarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    String sbarcode=txtbarcode.getText().toString();
                    ReadBarcode(sbarcode);
                    txtbarcode.setText("");
                    txtbarcode.requestFocus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (txtbarcode != null) {
                                txtbarcode.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                    return true;
                }
                return false;
            }
        });
/*
        txtbarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                   String sbarcode=txtbarcode.getText().toString();
                    ReadBarcode(sbarcode);
                    txtbarcode.setText("");
                    txtbarcode.requestFocus();

                    return true;
                }
                else{
                    txtbarcode.requestFocus();
                    return false;
                }
            }
        });
*/

        btnfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItem(true);
            }
        });
        btnadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItem(false);
            }
        });
        btnreadbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String barcode = txtbarcode.getText().toString();
                txtbarcode.setText("");
                ReadBarcode(barcode);
            }
        });


        btnsaveinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=new AlertDialog.Builder(NewPOSActivity.this)
                        .setMessage("Are you sure to save?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(Common._mrdtls.size()==0)
                                {
                                    Toast.makeText(NewPOSActivity.this,"Atleast one row must be entered!",Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(Double.valueOf(txtbillamount.getText().toString())<=0)
                                {
                                    Toast.makeText(NewPOSActivity.this,"Bill amount can't be zero or negative!",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                SaveData();
                            }
                        })
                        .setNegativeButton("No",null).create();
                dialog.show();
            }
        });

    }
    private void AddItem(Boolean blnFavourite)
    {
        final ArrayList<Item> items=new ArrayList<>();
        final ArrayList<Unit> units=new ArrayList<>();
        final LayoutInflater inflater=getLayoutInflater();
        View DialogView=inflater.inflate(R.layout.activity_add_item,null);
        final Button btnAdd=DialogView.findViewById(R.id.btnAdd);
        final AutoCompleteTextView txtproduct=DialogView.findViewById(R.id.txtproduct);
        final Button DecrementButton=DialogView.findViewById(R.id.DecrementButton);
        final Button IncrementButton=DialogView.findViewById(R.id.IncrementButton);
        final TextView txtaddnewcap=DialogView.findViewById(R.id.txtaddnewcap);
        txtqty=DialogView.findViewById(R.id.txtqty);
        final Spinner txtunit=DialogView.findViewById(R.id.txtunit);
        txtrate=DialogView.findViewById(R.id.txtrate);
        txtamount =DialogView.findViewById(R.id.txtamount);
        txtqty.setText("1");
        final AlertDialog alert=new AlertDialog.Builder(NewPOSActivity.this).create();
        if(!blnFavourite)
        {
            txtaddnewcap.setText("Add New Item");
        }
        else
        {
            txtaddnewcap.setText("Add New Favourite Item");
        }

        txtproduct.requestFocus();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtproduct.getText().toString().trim().equals(""))
                {
                    txtproduct.setError("Must select product");
                    txtproduct.requestFocus();
                    return;
                }
                if(txtproduct.getTag()==null)
                {
                    txtproduct.setError("Invalid product");
                    txtproduct.requestFocus();
                    return;
                }
                if(txtqty.getText().toString().trim().equals(""))
                {
                    txtqty.setError("Invalid Qty");
                    txtqty.requestFocus();
                    return;
                }
                if(txtrate.getText().toString().trim().equals(""))
                {
                   txtrate.setError("Invalid Rate");
                    txtrate.requestFocus();
                   return;
                }
                Double dblRate=Double.valueOf(txtrate.getText().toString());
                Double dblQty=Double.valueOf(txtqty.getText().toString());
                if(dblRate<=0)
                {
                    txtrate.setError("Invalid Rate");
                    txtrate.requestFocus();
                    return;
                }
                if(dblQty<=0)
                {
                    txtqty.setError("Invalid Qty");
                    txtqty.requestFocus();
                    return;
                }
                Item obj=new Item();
                if(txtproduct.getTag() instanceof Item)
                {
                    obj=(Item)txtproduct.getTag();
                }
                MRDTL _dtl = new MRDTL();
                _dtl.set_slno(getMaxSlno());
                _dtl.set_productid(obj.getId());
                _dtl.set_ProductCode(obj.getCode());
                _dtl.set_ProductName(obj.getName());
                _dtl.set_Qty(dblQty);
                _dtl.set_Rate(dblRate);
                _dtl.set_unitid(Integer.valueOf(txtunit.getTag().toString()));
                _dtl.set_Unit(txtunit.getSelectedItem().toString());
                Common._mrdtls.add(_dtl);
                CalcTotals();
                Collections.sort(Common._mrdtls,new PosDtlComparator());
                _posDtlAdapter =new MRDTLAdapter(NewPOSActivity.this,Common._mrdtls);
                grdnewpos.setAdapter(_posDtlAdapter);
                _posDtlAdapter.notifyDataSetChanged();
                txtbarcode.requestFocus();
                alert.dismiss();

            }
        });

        DecrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double dblQty=0.0;
                if(!txtqty.getText().toString().trim().equals(""))
                {
                    dblQty=Double.valueOf(txtqty.getText().toString());
                    if(dblQty>1)
                    {
                        dblQty=dblQty-1;
                        txtqty.setText(String.format(Common.sDecimals,dblQty));
                        CalcAmount();
                    }

                }
            }
        });
        IncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double dblQty=0.0;
                if(!txtqty.getText().toString().trim().equals(""))
                {
                    dblQty=Double.valueOf(txtqty.getText().toString());
                        dblQty=dblQty+1;
                        txtqty.setText(String.format(Common.sDecimals,dblQty));
                        CalcAmount();
                }
            }
        });

        String url="";
        if(blnFavourite)
        {
            url= "http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/GetFavourites";
        }
        else
        {
            url= "http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/GetItems";
        }

        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONArray jsonArray = response;
                    items.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Item _item = new Item();
                        _item.setId(jsonObject.getInt("id"));
                        _item.setCode(jsonObject.getString("Code"));
                        _item.setName(jsonObject.getString("Name"));
                        items.add(_item);
                    }
                    Item[] itemsArray = items.toArray(new Item[items.size()]);
                    ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(NewPOSActivity.this, android.R.layout.simple_list_item_1, itemsArray);
                    txtproduct.setAdapter(adapter);
                    txtproduct.setThreshold(0);

                }
                catch (Exception ex)
                {
                    Toast.makeText(NewPOSActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPOSActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);

        txtproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item _obj=(Item)adapterView.getItemAtPosition(i);
                if(_obj!=null)
                {
                    txtproduct.setTag(_obj);
                    String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/GetUnits?productid=" + _obj.getId();
                    JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                JSONArray jsonArray = response;
                                units.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Unit _unit = new Unit();
                                    _unit.setId(jsonObject.getInt("id"));
                                    _unit.setName(jsonObject.getString("Name"));
                                    _unit.setCf(jsonObject.getDouble("CF"));
                                    units.add(_unit);
                                }

                                txtunit.setAdapter(null);
                                Unit[] unitsArray = units.toArray(new Unit[units.size()]);
                                ArrayAdapter<Unit> adapter = new ArrayAdapter<Unit>(NewPOSActivity.this, android.R.layout.simple_list_item_1, unitsArray);
                                txtunit.setAdapter(adapter);
                                if(units.size()>0) {
                                    txtunit.setSelection(0);
                                    txtunit.setTag(units.get(0).getId());
                                }
                                int iproductid=0;
                                int iUnitID=0;
                                if(txtproduct.getTag() instanceof Item)
                                {
                                    Item obj=(Item)txtproduct.getTag();
                                    iproductid = obj.getId();
                                }
                                if(txtunit.getTag()!=null && !txtunit.getTag().toString().equals(""))
                                {
                                    iUnitID= Integer.valueOf(txtunit.getTag().toString());
                                }
                                GetRate(iproductid,iUnitID);
                                CalcAmount();

                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(NewPOSActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(NewPOSActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(jsonArrayRequest);
                }
            }
        });

        txtunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit _obj=(Unit)adapterView.getItemAtPosition(i);
                if(_obj!=null) {
                    txtunit.setTag(_obj.getId());
                    int iproductid=0;
                    int iUnitID=0;
                    if(txtproduct.getTag()!=null) {
                       if(txtproduct.getTag() instanceof Item)
                        {
                            Item obj=(Item)txtproduct.getTag();
                            iproductid = obj.getId();
                        }
                    }
                    if(txtunit.getTag()!=null && !txtunit.getTag().toString().equals(""))
                    {
                        iUnitID= Integer.valueOf(txtunit.getTag().toString());
                    }
                    GetRate(iproductid,iUnitID);
                    CalcAmount();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       /* txtunit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/


        alert.setView(DialogView);
        alert.show();
    }
   private void GetRate(int productID,int UnitID)
   {
       String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/getRate?productid=" + productID + "&unitid=" + UnitID;
       JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {

                try {

                    if(response!=null)
                    {
                        Double dblRate=response.getDouble("SalesRate");
                        txtrate.setText(String.format(Common.sDecimals,dblRate));
                        CalcAmount();
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(NewPOSActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
       requestQueue.add(request);

   }
   private void CalcAmount()
   {
       Double dblQty=0.0;
       Double dblRate=0.0;
       if(!txtqty.getText().toString().equals("")) {
           dblQty = Double.valueOf(txtqty.getText().toString());
       }
       if(!txtrate.getText().toString().equals("")) {
           dblRate = Double.valueOf(txtrate.getText().toString());
       }
       txtamount.setText(String.format(Common.sDecimals,dblQty*dblRate));
   }
   private void ClearAll()
   {
       blnNewRecord=true;
       if(Common.iPartyID!=0)
       {
           sPartyName=Common.sPartyName;
           iPartyID=Common.iPartyID;
       }
       blnSavingStart=false;
       tvparty.setText(sPartyName);
       tvparty.setTag(iPartyID);
       tvloginuser.setText(Common.currentUser.getName());
       tvloginuser.setTag(Common.currentUser.getId());
       dtdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
       txtbillamount.setText("0.00");
       Common._mrdtls=new ArrayList<>();
       _posDtlAdapter =new MRDTLAdapter(NewPOSActivity.this,Common._mrdtls);
       grdnewpos.setAdapter(_posDtlAdapter);
       _posDtlAdapter.notifyDataSetChanged();
       tvvoucherno.setTag("0");

       CalcTotals();
   }

   private void GetData(int iHdrID)
   {
       blnNewRecord=true;
       if(sIpAddress.equals(""))
       {
           Toast.makeText(NewPOSActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
           return;
       }
       ClearAll();
       String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/GetMR?hdrid=" + iHdrID;
       JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
           @Override
           public void onResponse(JSONArray response) {

               JSONArray jsonArray = response;
               try {

                   Common._mrdtls.clear();
                   JSONObject objHdr=jsonArray.getJSONObject(0);
                   if(objHdr!=null)
                   {
                       blnNewRecord=false;
                       dtdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                       txtbillamount.setText(String.format(Common.sDecimals,objHdr.getDouble("GrandTotal")));
                       tvvoucherno.setText(objHdr.getString("VoucherNo"));
                       tvvoucherno.setTag(objHdr.getInt("hdrid"));
                       tvparty.setText(objHdr.getString("PartyName"));
                       tvparty.setTag(objHdr.getInt("PartyID"));
                       tvloginuser.setText(Common.currentUser.getName());
                       tvloginuser.setTag(Common.currentUser.getId());
                       dtdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                       tvparty.setText(objHdr.getString("PartyName"));

                       int iPartyID=objHdr.getInt("PartyID");
                       tvparty.setTag(iPartyID);
                   }
                   Common._mrdtls=new ArrayList<>();
                   for(int i=0;i<jsonArray.length();i++)
                   {
                       JSONObject jsonObject = jsonArray.getJSONObject(i);
                       MRDTL pos=new MRDTL();
                       pos.set_slno(jsonObject.getInt("SlNo"));
                       pos.set_MRHdrid(jsonObject.getInt("hdrid"));
                       pos.set_Unit(jsonObject.getString("Unit"));
                       pos.set_unitid(jsonObject.getInt("UnitID"));
                       pos.set_Rate(jsonObject.getDouble("Rate"));
                       pos.set_Qty(jsonObject.getDouble("Qty"));
                       pos.set_productid(jsonObject.getInt("ProductID"));
                       pos.set_ProductName(jsonObject.getString("ProductName"));
                       pos.set_ProductCode(jsonObject.getString("ProductCode"));
                       pos.set_Amount(pos.get_Qty()*pos.get_Rate());

                       Common._mrdtls.add(pos);
                   }
                   _posDtlAdapter =new MRDTLAdapter(NewPOSActivity.this,Common._mrdtls);
                   grdnewpos.setAdapter(_posDtlAdapter);
                   _posDtlAdapter.notifyDataSetChanged();
               }
               catch (Exception ex)
               {
                   Toast.makeText(NewPOSActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
       requestQueue.add(jsonArrayRequest);


   }
   private void SaveData()
   {
       if(sIpAddress.equals(""))
       {
           Toast.makeText(NewPOSActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
           return;
       }
       if(blnSavingStart){ return; }
       blnSavingStart=true;
       btnsaveinvoice.setVisibility(View.INVISIBLE);
       JSONObject jsnSaveData=new JSONObject();
       JSONArray jsnArray=new JSONArray();
       for(MRDTL _dtl :Common._mrdtls)
       {
           JSONObject obj=new JSONObject();
           try {
               obj.put("hdrid",Integer.valueOf(tvvoucherno.getTag().toString()));
               obj.put("FiscalID",Common.iFiscalID);
               obj.put("BranchID",Common.CurrentBranchID);
               obj.put("DepoID",Common.CurrentDepoID);
               obj.put("LoginUserID",Common.currentUser.getId());
               obj.put("VoucherNo",tvvoucherno.getText().toString());
               obj.put("PurchaseType",Common.iPurchaseTypeID);
               obj.put("PartyID",Integer.valueOf(tvparty.getTag().toString()));
               obj.put("GrandTotal",Double.valueOf(txtbillamount.getText().toString()));
               obj.put("ProductID",_dtl.get_productid());
               obj.put("Qty",_dtl.get_Qty());
               obj.put("Rate",_dtl.get_Rate());
               obj.put("UnitID",_dtl.get_unitid());
               if(blnNewRecord)
               {
                   obj.put("NewRecord",1);
               }
               else
               {
                   obj.put("NewRecord",0);
               }


               jsnArray.put(obj);
           }
           catch (JSONException e)
           {
               e.printStackTrace();
           }
       }
       String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/savedata";
       JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, url, jsnArray, new Response.Listener<JSONArray>() {
           @Override
           public void onResponse(JSONArray response) {
               if(response!=null) {
                   try {
                       JSONObject _jsnresponse=response.getJSONObject(0);
                       String sVoucherNo= _jsnresponse.getString("VoucherNo");
                       Integer iStatus=_jsnresponse.getInt("Status");
                       if(iStatus==1) {
                           if(blnNewRecord) {
                               Toast.makeText(NewPOSActivity.this, "Material Receipt saved with Voucher No " + sVoucherNo, Toast.LENGTH_LONG).show();
                           }
                           else
                           {
                               Toast.makeText(NewPOSActivity.this, "Material Receipt Updated with Voucher No " + sVoucherNo, Toast.LENGTH_LONG).show();
                           }
                           ClearAll();
                           getVoucherNo();
                           btnsaveinvoice.setVisibility(View.VISIBLE);
                       }
                       else if(iStatus==2)
                       {
                           String sError=_jsnresponse.getString("ErrorString");
                           String sMessage="Error while printing ! " +sError;
                           Toast.makeText(NewPOSActivity.this, sMessage, Toast.LENGTH_LONG).show();
                           blnSavingStart=false;
                           btnsaveinvoice.setVisibility(View.VISIBLE);
                       }
                       else
                       {
                           String sError=_jsnresponse.getString("ErrorString");
                           String sMessage="Material Receipt Saving Failed! " +sError;
                           Toast.makeText(NewPOSActivity.this, sMessage, Toast.LENGTH_LONG).show();
                           blnSavingStart=false;
                           btnsaveinvoice.setVisibility(View.VISIBLE);

                       }
                   }
                   catch (JSONException e)
                   {
                       e.printStackTrace();
                       blnSavingStart=false;
                       btnsaveinvoice.setVisibility(View.VISIBLE);
                   }
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(NewPOSActivity.this, "MR Saving failed!", Toast.LENGTH_LONG).show();
               btnsaveinvoice.setVisibility(View.VISIBLE);
           }
       });
       jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
               0,
               DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
               DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       requestQueue.add(jsonArrayRequest);

   }
   private  int getMaxSlno()
   {
       int islno=0;
       int itotslno=1;
       for(MRDTL dtl:Common._mrdtls)
       {
           if(dtl.get_productid()!=0) {
               itotslno = itotslno + 1;
           }
       }
       return itotslno;
   }
    private  void ReadBarcode(String sBarcode)
    {
        if(sIpAddress.equals(""))
        {
            Toast.makeText(NewPOSActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
            txtbarcode.requestFocus();
            return;
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/readBarcode?Barcode=" + sBarcode;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if(response!=null)
                    {
                        if(response.getInt("ProductID")!=0) {
                            MRDTL _dtl = new MRDTL();
                            _dtl.set_slno(getMaxSlno());
                            _dtl.set_productid(response.getInt("ProductID"));
                            _dtl.set_ProductCode(response.getString("ProductCode"));
                            _dtl.set_ProductName(response.getString("ProductName"));
                            _dtl.set_Qty(response.getDouble("Qty"));
                            _dtl.set_Rate(response.getDouble("PurchaseRate"));
                            _dtl.set_unitid(response.getInt("UnitID"));
                            _dtl.set_Unit(response.getString("UnitName"));

                            Common._mrdtls.add(_dtl);
                            CalcTotals();
                        }
                        else
                        {
                            textToSpeech.speak("Product Not Found!",TextToSpeech.QUEUE_FLUSH,null);
                            txtbarcode.setError("Product Not Found!");
                        }
                        txtbarcode.setText("");
                        Collections.sort(Common._mrdtls,new PosDtlComparator());
                        _posDtlAdapter =new MRDTLAdapter(NewPOSActivity.this,Common._mrdtls);
                        grdnewpos.setAdapter(_posDtlAdapter);
                        _posDtlAdapter.notifyDataSetChanged();
                        txtbarcode.requestFocus();

                    }
                    else
                    {
                        textToSpeech.speak("Barcode Reading Failed!",TextToSpeech.QUEUE_FLUSH,null);
                        Toast.makeText(getApplicationContext(),"Barcode Reading Failed!",Toast.LENGTH_LONG).show();
                        txtbarcode.requestFocus();
                    }
                }
                catch (Exception w)
                {
                    textToSpeech.speak("Barcode Reading Failed!",TextToSpeech.QUEUE_FLUSH,null);
                    Toast.makeText(getApplicationContext(),w.getMessage(),Toast.LENGTH_LONG).show();
                    txtbarcode.requestFocus();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPOSActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                txtbarcode.requestFocus();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
    private void getVoucherNo()
    {
        if(sIpAddress.equals(""))
        {
            Toast.makeText(NewPOSActivity.this,"IP address can't be blank!",Toast.LENGTH_LONG).show();
            return;
        }
        String url="http://" + sIpAddress + "/" + Common.DomainName + "/api/mr/getvoucherno?FiscalID=" + Common.iFiscalID + "&PurchaseTypeID=" + Common.iPurchaseTypeID;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(response!=null)
                    {
                        String sResult=response.replace("\"", "");
                        tvvoucherno.setText(sResult);

                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(NewPOSActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPOSActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }
    private void CalcTotals()
    {
        Double dblNetAmount=0.0;
        for(MRDTL dtl: Common._mrdtls)
        {
            dtl.set_Amount(dtl.get_Qty()*dtl.get_Rate());
            dblNetAmount=dblNetAmount + (dtl.get_Qty()*dtl.get_Rate());
        }
        txtbillamount.setText(String.format(Common.sDecimals,dblNetAmount));
    }
     @Override
    protected void onResume() {
        super.onResume();

         _posDtlAdapter =new MRDTLAdapter(NewPOSActivity.this,Common._mrdtls);
         grdnewpos.setAdapter(_posDtlAdapter);
         _posDtlAdapter.notifyDataSetChanged();
        CalcTotals();
    }
    @Override
    public void onBackPressed() {

        AlertDialog dialog=new AlertDialog.Builder(NewPOSActivity.this)
                .setMessage("Are you sure to close?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No",null).create();
        dialog.show();
    }


}