package com.example.asterisk.firebaseexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewAdapter extends BaseAdapter{
    List<String> data = new ArrayList<>();
    Context c;
    public NewAdapter(StorageActivity storageActivity, List<String> image) {
    data = image;
    c = storageActivity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(c).inflate(R.layout.imagelayout,null);
        TextView tview =  convertView.findViewById(R.id.image);
        tview.setText(data.get(position));
        return convertView;
    }

}
