package com.example.admin.phanthingoc_buoi13;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 5/1/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class AdapterFile extends BaseAdapter {

    private ArrayList<File> arrFile;
    private LayoutInflater inflater;
    private java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("MM/dd/yyyy   HH:mm:ss ");
    public AdapterFile(ArrayList<File> arrFile, Context context) {
        this.arrFile = arrFile;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrFile.size();
    }

    @Override
    public Object getItem(int position) {
        return arrFile.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder viewHolder;
        if (v==null) {
            v=inflater.inflate(R.layout.item_file, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.imFile= (ImageView) v.findViewById(R.id.imFile);
            viewHolder.tvName= (TextView) v.findViewById(R.id.tvName);
            viewHolder.tvPath= (TextView) v.findViewById(R.id.tvPath);
            v.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) v.getTag();
        }
        File file=arrFile.get(position);
        viewHolder.tvName.setText(file.getName());
        viewHolder.tvPath.setText(dateFormat.format(file.lastModified()));

        if (file.isDirectory()) {
            viewHolder.imFile.setImageResource(R.drawable.folder);
        }else {
            viewHolder.imFile.setImageResource(R.drawable.file);
        }
        return v;
    }

    class ViewHolder{
        ImageView imFile;
        TextView tvName;
        TextView tvPath;
    }
}
