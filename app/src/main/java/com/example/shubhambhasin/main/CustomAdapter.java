package com.example.shubhambhasin.main;

/**
 * Created by Shubham Bhasin on 26-Mar-16.
 */
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Model> implements View.OnClickListener{
        Model[] modelItems = null;
        Context context;
    public CustomAdapter(Context context, Model[] resource) {
        super(context,R.layout.single_list_row,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
        }
@Override
public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

    Model item = (Model) this.getItem(position);
    TextView name;
    CheckBox cb;
    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
    if (convertView == null){

        convertView = inflater.inflate(R.layout.single_list_row, parent, false);
        name = (TextView) convertView.findViewById(R.id.textView1);
        name.setOnClickListener(this);
        cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        cb.setOnClickListener(this);
        convertView.setTag(new ModelViewHolder(name, cb));




    }else{
        ModelViewHolder viewHolder = (ModelViewHolder) convertView
                .getTag();
        cb = viewHolder.getCheckBox();
        name = viewHolder.getTextView();
    }


    name.setText(modelItems[position].getName());
    if (modelItems[position].getValue() == 1)
        cb.setChecked(true);
    else
        cb.setChecked(false);

    cb.setTag(item);
    return convertView;
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox1:
                // Do stuff accordingly...
                CheckBox cb = (CheckBox) v;
                Model model = (Model) cb.getTag();
                model.setChecked(cb.isChecked());
                break;
            default:
                break;
        }
    }
}