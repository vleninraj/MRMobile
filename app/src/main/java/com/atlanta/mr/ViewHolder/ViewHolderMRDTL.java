package com.atlanta.mr.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atlanta.mr.R;

public class ViewHolderMRDTL {
    public TextView tvproductname,tvqty,tvunit,tvsalesrate,tvnewposamount,tvproductcode;
    public Button btnposdecrement,btnposincrement,btnremoveitem;

    public   ViewHolderMRDTL(View _view)
    {
        tvproductname=(TextView) _view.findViewById(R.id.tvproductname);
        tvqty=(TextView)_view.findViewById(R.id.tvqty);
        tvunit=(TextView)_view.findViewById(R.id.tvunit);
        tvsalesrate=(TextView)_view.findViewById(R.id.tvsalesrate);
        tvnewposamount=(TextView)_view.findViewById(R.id.tvnewposamount);
        btnposdecrement=(Button) _view.findViewById(R.id.btnposdecrement);
        btnposincrement=(Button) _view.findViewById(R.id.btnposincrement);
        btnremoveitem=(Button) _view.findViewById(R.id.btnremoveitem);
        tvproductcode=(TextView)_view.findViewById(R.id.tvproductcode);
    }

}
