package com.example.shubhambhasin.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;

public class popularhorizontallist extends Fragment {

    public static TextView user;
    public static ImageButton searchButton;
    public static EditText searchText;

    Gallery myHorizontalListView;

    public String[] names;
    public HashMap<Integer,Bitmap> Images;
    public HashMap<String,String> item_map;

    public void setData(final String[] names, final HashMap<Integer,Bitmap> Images, final HashMap<String,String> item_map) {
        this.names=names;
        this.Images=Images;
        this.item_map=item_map;
        HorizontalList adapter = new HorizontalList(getActivity(), names, Images);
        myHorizontalListView.setAdapter(adapter);

        myHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                String item=names[position];
                // Toast.makeText(getApplicationContext(),category_map.get(item),Toast.LENGTH_LONG).show();
                ParseObject itemobject=ParseObject.createWithoutData(ItemTable.TABLE_NAME,item_map.get(item));
                ParseObject subcatobject=itemobject.getParseObject(ItemTable.SUB_CATEGORY);
                String subcategoryId=subcatobject.getObjectId();
                String subcategoryName = subcatobject.fetchIfNeeded().getString(SubCategoryTable.NAME);

                Intent categorySelectedIntent=new Intent(getActivity(),itemDetails.class);
                categorySelectedIntent.putExtra("itemId", item_map.get(item));
                categorySelectedIntent.putExtra("subcategoryId", subcategoryId);
                categorySelectedIntent.putExtra("subcategoryName", subcategoryName);
                startActivity(categorySelectedIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_popularhorizontallist, container, false);

        myHorizontalListView=(Gallery)layout.findViewById(R.id.horizontallistview);



        return layout;


    }


}
