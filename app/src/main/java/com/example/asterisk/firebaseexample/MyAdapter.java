package com.example.asterisk.firebaseexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

class MyAdapter extends BaseAdapter {
    Context c;
    List<DataModule> mylist;
    public MyAdapter(Dashboard dashboard, List<DataModule> list) {
        c = dashboard;
        mylist = list;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(c).inflate(R.layout.newlayout,null);
        TextView mname = convertView.findViewById(R.id.mname);
        TextView mroll = convertView.findViewById(R.id.mroll);
        mroll.setText(String.valueOf(mylist.get(position).getRoll()));
        mname.setText(String.valueOf(mylist.get(position).getName()));
        return convertView;
    }
}
