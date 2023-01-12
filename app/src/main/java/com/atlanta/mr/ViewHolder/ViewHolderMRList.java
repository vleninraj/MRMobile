package com.atlanta.mr.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.atlanta.mr.R;

public class ViewHolderMRList {

    public TextView tvvoucherno,tvparty,tvvoucherdate,tvgrandamount;
  public   ViewHolderMRList(View _view)
    {
        tvvoucherno=(TextView) _view.findViewById(R.id.tvvoucherno);
        tvparty=(TextView)_view.findViewById(R.id.tvparty);
        tvgrandamount=(TextView)_view.findViewById(R.id.tvgrandamount);
        tvvoucherdate=(TextView)_view.findViewById(R.id.tvvoucherdate);
    }
}
