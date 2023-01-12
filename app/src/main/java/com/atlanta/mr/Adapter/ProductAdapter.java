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
import com.atlanta.mr.R;
import com.atlanta.mr.Models.Item;
import com.atlanta.mr.ViewHolder.ViewHolderItem;

import java.util.ArrayList;
import java.util.Base64;

public class ProductAdapter extends BaseAdapter {
    private static final String TAG = "GroupAdapter";
    Activity _context;
    ArrayList<Item> _products;

    public ProductAdapter(Activity  context, ArrayList<Item> products) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._products=products;
    }
    @Override
    public int getCount() {
        return _products.size();
    }

    @Override
    public Object getItem(int i) {
        return _products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        ViewHolderItem _viewholder=null;
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.activity_productadapter,viewGroup,false);
            _viewholder=new ViewHolderItem(vw);
            vw.setTag(_viewholder);
        }
        else{
            _viewholder=(ViewHolderItem) vw.getTag();
        }

        final Item _product = (Item) this.getItem(i);
        _viewholder.txtproductcode .setText(_product.getCode());
        _viewholder.txtproductname.setTag(_product.getId());
        _viewholder.txtproductname .setText(_product.getName());
        _viewholder.txtunit.setText(_product.getUnit());
        _viewholder.txtsalesrate.setText(String.format(Common.sDecimals,_product.getPurchaseRate()));


        Log.d(TAG, "From View" + _product.getName());
        return vw;
    }
}
