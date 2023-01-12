package com.atlanta.mr.ViewHolder;

import android.view.View;
import android.widget.TextView;
import com.atlanta.mr.R;
public class ViewHolderItem {

public TextView  txtproductcode,txtproductname,txtsalesrate,txtunit;
    public   ViewHolderItem(View _view)
    {
        txtproductcode=(TextView)_view.findViewById(R.id.txtproductcode);
        txtproductname=(TextView)_view.findViewById(R.id.txtproductname);
        txtsalesrate=(TextView) _view.findViewById(R.id.txtsalesrate);
        txtunit=(TextView) _view.findViewById(R.id.txtunit);


    }

}
