package com.example.shubhambhasin.main;

/**
 * Created by Shubham Bhasin on 23-Mar-16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class CustomList extends BaseAdapter{
    private Context mContext;
    private final String[] names;
    private final HashMap<Integer,Bitmap> Images;
    String type;

    public CustomList(Context c,String[] names,HashMap<Integer,Bitmap> Images,String type) {
        this.type=type;
        mContext = c;
        this.Images = Images;
        this.names =names;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);

        } else {
            grid = (View) convertView;
        }

        grid = inflater.inflate(R.layout.single_list_item, null);
        TextView textView = (TextView) grid.findViewById(R.id.list_text);
        ImageView imageView = (ImageView)grid.findViewById(R.id.list_image);
        textView.setText(names[position]);
        if(type.equalsIgnoreCase("heading"))
        {
            textView.setAllCaps(true);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
        }
        imageView.setImageBitmap(Images.get(position));
        if(position%2==0){
            grid.setBackgroundColor(Color.rgb(255,255,255));
        }else
        {
            grid.setBackgroundColor(Color.rgb(183, 183, 183));
            textView.setTextColor(Color.WHITE);
        }

        return grid;
    }
}