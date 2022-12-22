package com.atlanta.mr.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.atlanta.mr.Common;
import com.atlanta.mr.Models.POSList;
import com.atlanta.mr.R;
import com.atlanta.mr.ViewHolder.ViewHolderPOSList;

import java.util.ArrayList;

public class POSListAdapter extends BaseAdapter {
    private static final String TAG = "POSList";
    Activity _context;
    ArrayList<POSList> _poslists;

    public POSListAdapter(Activity  context, ArrayList<POSList> poslists) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._poslists=poslists;
    }
    @Override
    public int getCount() {
        return _poslists.size();
    }

    @Override
    public Object getItem(int i) {
        return _poslists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderPOSList _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.activity_orderlistadapter,viewGroup,false);
            _viewholder=new ViewHolderPOSList(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderPOSList) vw.getTag();
        }

        final POSList _poslist = (POSList) this.getItem(i);
        _viewholder.tvvoucherno.setText(_poslist.getVoucherNo());
        _viewholder.tvvoucherno.setTag(_poslist.getId());
        _viewholder.tvvoucherdate.setText(_poslist.getVoucherDate());
        _viewholder.tvparty.setText(_poslist.getParty());
        _viewholder.tvgrandamount.setText(String.format( Common.sDecimals, _poslist.getGrandTotal()));
        Log.d(TAG, "From View" + _poslist.getVoucherNo());
        return vw;
    }
}
