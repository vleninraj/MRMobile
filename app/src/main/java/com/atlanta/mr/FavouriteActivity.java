package com.atlanta.mr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.atlanta.mr.Adapter.MRDTLAdapter;
import com.atlanta.mr.Adapter.MRListAdapter;
import com.atlanta.mr.Adapter.ProductAdapter;
import com.atlanta.mr.Models.Item;
import com.atlanta.mr.Models.MRDTL;
import com.atlanta.mr.Models.MRList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class FavouriteActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);


    }

}