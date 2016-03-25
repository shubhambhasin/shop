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

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final String[] names;
    private final HashMap<Integer,Bitmap> Images;

    public CustomGrid(Context c,String[] names,HashMap<Integer,Bitmap> Images) {
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
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);

        } else {
            grid = (View) convertView;
        }

        grid = inflater.inflate(R.layout.single_grid_item, null);
        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
        textView.setText(names[position]);
        imageView.setImageBitmap(Images.get(position));
        if(position%4==0){
            grid.setBackgroundColor(Color.rgb(255, 255, 255));
        }else if(position%4==1)
        {
            grid.setBackgroundColor(Color.rgb(193,193,193));
        }else if(position%4==2)
        {
            grid.setBackgroundColor(Color.rgb(193,193,193));
        }else
        {
            grid.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        return grid;
    }
}