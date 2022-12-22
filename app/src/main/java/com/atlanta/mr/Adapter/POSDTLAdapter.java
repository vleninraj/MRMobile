package com.atlanta.mr.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.atlanta.mr.Common;
import com.atlanta.mr.Models.POSDTL;
import com.atlanta.mr.R;
import com.atlanta.mr.ViewHolder.ViewHolderPOSDTL;

import java.util.ArrayList;
import java.util.Collections;

public class POSDTLAdapter extends BaseAdapter {

    private static final String TAG = "POSDTLAdapter";
    Activity _context;
    ArrayList<POSDTL> _positems;

    public POSDTLAdapter(Activity  context, ArrayList<POSDTL> positems) {
        //   super(c, R.layout.listview,workid);
        this._context=context;
        this._positems=positems;
    }


    @Override
    public int getCount() {
        return _positems.size();
    }

    @Override
    public Object getItem(int i) {
        return _positems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vw=view;
        final POSDTL _positem = (POSDTL) this.getItem(i);
        if(vw==null)
        {
            LayoutInflater layoutInflater=_context.getLayoutInflater();
            vw=layoutInflater.inflate(R.layout.activity_posdtladapter,viewGroup,false);
            final ViewHolderPOSDTL _viewholder=new ViewHolderPOSDTL(vw);
            vw.setTag(_viewholder);
            _viewholder.btnposincrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double dblQty=_positem.get_Qty();
                    dblQty++;
                    _positem.set_Qty(dblQty);
                    _viewholder.tvqty.setText(String.format(Common.sDecimalsQty,_positem.get_Qty()));
                    Double dblRate=Double.valueOf(_viewholder.tvsalesrate.getText().toString());
                    _viewholder.tvnewposamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
                    _positem.set_Qty(Double.valueOf(_viewholder.tvqty.getText().toString()));
                    _positem.set_Rate(Double.valueOf(_viewholder.tvsalesrate.getText().toString()));
                    _positem.set_Amount(Double.valueOf(_viewholder.tvnewposamount.getText().toString()));
                    CalcTotals();
                }
            });
            _viewholder.btnposdecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double dblQty=_positem.get_Qty();
                    if(!dblQty.equals(1))
                    {
                        dblQty--;
                        _positem.set_Qty(dblQty);
                        _viewholder.tvqty.setText(String.format(Common.sDecimalsQty,_positem.get_Qty()));
                        Double dblRate=Double.valueOf(_viewholder.tvsalesrate.getText().toString());
                        _viewholder.tvnewposamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
                        _positem.set_Qty(Double.valueOf(_viewholder.tvqty.getText().toString()));
                        _positem.set_Rate(Double.valueOf(_viewholder.tvsalesrate.getText().toString()));
                        _positem.set_Amount(Double.valueOf(_viewholder.tvnewposamount.getText().toString()));
                        CalcTotals();
                    }

                }
            });

              _viewholder.btnremoveitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                       RemoveItem(i);
                }
            });

            _viewholder.tvproductname.setText(_positem.get_ProductName());
            _viewholder.tvproductname.setTag(_positem.get_productid());
            _viewholder.tvproductcode.setText(_positem.get_ProductCode());
            _viewholder.tvqty.setText(String.format(Common.sDecimalsQty,_positem.get_Qty()));
            _viewholder.tvsalesrate.setText(String.format(Common.sDecimals,_positem.get_Rate()));
            _viewholder.tvunit.setText(_positem.get_Unit());
            _viewholder.tvunit.setTag(_positem.get_unitid());
            _viewholder.btnposincrement.setTag(_positem.get_id());

            Double dblQty=Double.valueOf(_viewholder.tvqty.getText().toString());
            Double dblRate=Double.valueOf(_viewholder.tvsalesrate.getText().toString());
            _viewholder.tvnewposamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
            _positem.set_Qty(Double.valueOf(_viewholder.tvqty.getText().toString()));
            _positem.set_Rate(Double.valueOf(_viewholder.tvsalesrate.getText().toString()));
            _positem.set_Amount(Double.valueOf(_viewholder.tvnewposamount.getText().toString()));

        }
        else{
            final ViewHolderPOSDTL _viewholder=(ViewHolderPOSDTL) vw.getTag();
            _viewholder.tvproductname.setText(_positem.get_ProductName());
            _viewholder.tvproductname.setTag(_positem.get_productid());
            _viewholder.tvproductcode.setText(_positem.get_ProductCode());
            _viewholder.tvqty.setText(String.format(Common.sDecimalsQty,_positem.get_Qty()));
            _viewholder.tvsalesrate.setText(String.format(Common.sDecimals,_positem.get_Rate()));
            _viewholder.tvunit.setText(_positem.get_Unit());
            _viewholder.tvunit.setTag(_positem.get_unitid());
            _viewholder.btnposincrement.setTag(_positem.get_id());


            Double dblQty=Double.valueOf(_viewholder.tvqty.getText().toString());
            Double dblRate=Double.valueOf(_viewholder.tvsalesrate.getText().toString());
            _viewholder.tvnewposamount.setText(String.format(Common.sDecimals,dblRate*dblQty));
            _positem.set_Qty(Double.valueOf(_viewholder.tvqty.getText().toString()));
            _positem.set_Rate(Double.valueOf(_viewholder.tvsalesrate.getText().toString()));
            _positem.set_Amount(Double.valueOf(_viewholder.tvnewposamount.getText().toString()));

        }

        //   _viewholder.txtwaitername.setText(_orderitem.get_WaiterName());
        //  _viewholder.txtwaitername.setTag(_orderitem.get_id());
        // Log.d(TAG, "From View" + _orderitem.get_WaiterName());
        return vw;
    }
    public  void RemoveItem(int i)
    {
        _positems.remove(i);
        notifyDataSetChanged();
        CalcTotals();
    }
    private void CalcTotals()
    {
        Double dblNetAmount=0.0;
        for(POSDTL dtl: Common._posdtls)
        {
            dblNetAmount=dblNetAmount + (dtl.get_Qty()*dtl.get_Rate());
        }
        TextView txtbillamount=_context.findViewById(R.id.txtbillamount);
        txtbillamount.setText(String.format(Common.sDecimals,dblNetAmount));
    }
}
